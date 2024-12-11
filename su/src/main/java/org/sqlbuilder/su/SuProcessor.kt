package org.sqlbuilder.su

import org.sqlbuilder.common.AlreadyFoundException
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.NotFoundException
import org.sqlbuilder.common.Processor
import org.sqlbuilder.common.Progress.NONE
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.traceSaving
import org.sqlbuilder.su.joins.Formula_Arg
import org.sqlbuilder.su.joins.Formula_Arg.Companion.make
import org.sqlbuilder.su.joins.Term_Synset
import org.sqlbuilder.su.joins.Term_Synset.Companion.parse
import org.sqlbuilder.su.objects.Formula
import org.sqlbuilder.su.objects.Formula.Companion.make
import org.sqlbuilder.su.objects.SUFile
import org.sqlbuilder.su.objects.Term
import org.sqlbuilder.su.objects.Term.Companion.parse
import org.sqlbuilder.su.objects.TermAttr
import org.sqlbuilder.su.objects.TermAttr.Companion.make
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.text.ParseException
import java.util.*
import kotlin.Throws

open class SuProcessor(conf: Properties) : Processor("sumo") {

    protected val inDir: File = File(conf.getProperty("su_home", System.getenv()["SUMOHOME"]))

    protected val names: Names = Names("su")

    protected var header: String = conf.getProperty("su_header")

    protected var termsColumns: String = names.columns("terms")

    protected var synsetsColumns: String = names.columns("terms_synsets")

    protected var resolve: Boolean = false

    protected var outDir: File = File(conf.getProperty("su_outdir", "sql/data"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    open fun processTerms(ps: PrintStream, terms: Iterable<Term>, table: String, columns: String) {
        insertTerms(ps, terms, table, columns)
    }

    open fun processTermsAndAttrs(ps: PrintStream, ps2: PrintStream, terms: Iterable<Term>, kb: Kb, table: String, columns: String, table2: String, columns2: String) {
        insertTermsAndAttrs(ps, ps2, terms, kb, table, columns, table2, columns2)
    }

    open fun processSynsets(ps: PrintStream, terms_synsets: Iterable<Term_Synset>, table: String, columns: String) {
        insertSynsets(ps, terms_synsets, table, columns)
    }

    // R U N

    @Throws(IOException::class)
    override fun run() {
        KBLoader().load()
        checkNotNull(KBLoader.kb)
        collectFiles(KBLoader.kb!!)
        collectTerms(KBLoader.kb!!)
        collectFormulas(KBLoader.kb!!)
        collectSynsets(inDir.toString() + File.separator + SUMO_TEMPLATE, WARNINGS)

        try {
            SUFile.COLLECTOR.open().use {
                Term.COLLECTOR.open().use {
                    Formula.COLLECTOR.open().use {
                        traceSaving("files")
                        PrintStream(FileOutputStream(File(outDir, names.file("files"))), true, StandardCharsets.UTF_8).use { ps ->
                            ps.println("-- $header")
                            insertFiles(ps, SUFile.COLLECTOR, names.table("files"), names.columns("files"))
                        }
                        traceDone()

                        traceSaving("terms")
                        PrintStream(FileOutputStream(File(outDir, names.file("terms"))), true, StandardCharsets.UTF_8).use { ps ->
                            PrintStream(FileOutputStream(File(outDir, names.file("terms_attrs"))), true, StandardCharsets.UTF_8).use { ps2 ->
                                ps.println("-- $header")
                                ps2.println("-- $header")
                                processTermsAndAttrs(ps, ps2, Term.COLLECTOR, KBLoader.kb!!, names.table("terms"), termsColumns, names.table("terms_attrs"), names.columns("terms_attrs"))
                            }
                        }
                        traceDone()

                        traceSaving("formulas")
                        PrintStream(FileOutputStream(File(outDir, names.file("formulas"))), true, StandardCharsets.UTF_8).use { ps ->
                            PrintStream(FileOutputStream(File(outDir, names.file("formulas_args"))), true, StandardCharsets.UTF_8).use { ps2 ->
                                ps.println("-- $header")
                                ps2.println("-- $header")
                                insertFormulasAndArgs(ps, ps2, Formula.COLLECTOR, names.table("formulas"), names.columns("formulas"), names.table("formulas_args"), names.columns("formulas_args"))
                            }
                        }
                        traceDone()

                        traceSaving("term-synsets")
                        PrintStream(FileOutputStream(File(outDir, names.file("terms_synsets"))), true, StandardCharsets.UTF_8).use { ps ->
                            ps.println("-- $header")
                            processSynsets(ps, Term_Synset.SET, names.table("terms_synsets"), synsetsColumns)
                        }
                        traceDone()
                    }
                }
            }
        } catch (e: NotFoundException) {
            throw RuntimeException(e)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }

    companion object {

        val WARNINGS = NONE

        private val POSES = arrayOf<String?>("noun", "verb", "adj", "adv")

        const val SUMO_TEMPLATE: String = "WordNetMappings/WordNetMappings30-%s.txt"

        // C O L L E C T

        fun collectFiles(kb: Kb) {
            for (filename in kb.filenames) {
                SUFile.make(filename)
            }
        }

        fun collectTerms(kb: Kb) {
            for (term in kb.terms) {
                Term.make(term)
            }
        }

        fun collectFormulas(kb: Kb) {
            for (formula in kb.formulas.values) {
                make(formula)
            }
        }

        @Throws(IOException::class)
        fun collectSynsets(fileTemplate: String, pse: PrintStream) {
            for (posName in POSES) {
                val filename = fileTemplate.format(posName)
                collectFileSynsets(filename, pse)
            }
        }

        @Throws(IOException::class)
        fun collectFileSynsets(filename: String, pse: PrintStream) {
            // iterate on synsets
            val path = Paths.get(filename)
            BufferedReader(InputStreamReader(Files.newInputStream(path))).use { reader ->
                var lineno = 0
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    lineno++
                    line = line!!.trim { it <= ' ' }
                    if (line.isEmpty() || line[0] == ' ' || line[0] == ';' || !line.contains("&%")) {
                        continue
                    }

                    // read
                    try {
                        val term = parse(line)
                        /* final Term_Sense mapping = */
                        parse(term, line) // side effect: term mapping collected into set
                    } catch (iae: IllegalArgumentException) {
                        pse.println(path.fileName.toString() + ':' + lineno + " " + ": ILLEGAL [" + iae.message + "] : " + line)
                    } catch (afe: AlreadyFoundException) {
                        pse.println(path.fileName.toString() + ':' + lineno + " " + ": DUPLICATE [" + afe.message + "] : " + line)
                    }
                }
            }
        }

        // I N S E R T

        fun insertFiles(ps: PrintStream, files: Iterable<SUFile>, table: String, columns: String) {
            val iterator: Iterator<SUFile> = files.iterator()
            if (iterator.hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                while (iterator.hasNext()) {
                    val file = iterator.next()
                    val isLast = !iterator.hasNext()
                    val row = file.dataRow()
                    ps.println("($row)${if (isLast) ";" else ","}")
                }
            }
        }

        fun insertTerms(ps: PrintStream, terms: Iterable<Term>, table: String, columns: String) {
            val iterator: Iterator<Term> = terms.iterator()
            if (iterator.hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                while (iterator.hasNext()) {
                    val term = iterator.next()
                    val isLast = !iterator.hasNext()
                    val row = term.dataRow()
                    ps.println("($row)${if (isLast) ";" else ","}")
                }
            }
        }

        fun insertTermsAndAttrs(ps: PrintStream, ps2: PrintStream, terms: Iterable<Term>, kb: Kb, table: String, columns: String, table2: String, columns2: String) {
            val iterator: Iterator<Term> = terms.iterator()
            if (iterator.hasNext()) {
                var i = 0
                ps.println("INSERT INTO $table ($columns) VALUES")
                ps2.println("INSERT INTO $table2 ($columns2) VALUES")
                while (iterator.hasNext()) {
                    val term = iterator.next()
                    val isLast = !iterator.hasNext()
                    val row = term.dataRow()
                    ps.println("($row)${if (isLast) ";" else ","}")

                    val termid = term.resolve()
                    try {
                        val attributes: Collection<TermAttr> = make(term, kb)
                        for (attribute in attributes) {
                            val row2 = "$termid,${attribute.dataRow()}"
                            val comment2 = term.comment()
                            ps2.print("${if (i == 0) "" else ",\n"}($row2) /* $comment2 */")
                            i++
                        }
                    } catch (_: NotFoundException) {
                    }
                }
                ps2.println(";")
            }
        }

        fun insertTermAttrs(ps: PrintStream, terms: Iterable<Term>, kb: Kb, table: String, columns: String) {
            val iterator: Iterator<Term> = terms.iterator()
            if (iterator.hasNext()) {
                var j = 0
                ps.println("INSERT INTO $table ($columns) VALUES")
                while (iterator.hasNext()) {
                    val term = iterator.next()
                    val termid = term.resolve()
                    try {
                        val attributes: Collection<TermAttr> = make(term, kb)
                        for (attribute in attributes) {
                            val row2 = "$termid,${attribute.dataRow()}"
                            val comment2 = term.comment()
                            ps.print("${if (j == 0) "" else ",\n"}($row2) /* $comment2 */")
                            j++
                        }
                    } catch (_: NotFoundException) {
                    }
                }
                ps.println(";")
            }
        }

        fun insertFormulas(ps: PrintStream, formulas: Iterable<Formula>, table: String, columns: String) {
            val iterator: Iterator<Formula> = formulas.iterator()
            if (iterator.hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                while (iterator.hasNext()) {
                    val formula = iterator.next()
                    val isLast = !iterator.hasNext()
                    val row = formula.dataRow()
                    ps.println("($row)${if (isLast) ";" else ","}")
                }
            }
        }

        @Throws(NotFoundException::class, ParseException::class, IOException::class)
        fun insertFormulasAndArgs(ps: PrintStream, ps2: PrintStream, formulas: Iterable<Formula>, table: String, columns: String, table2: String, columns2: String) {
            val iterator: Iterator<Formula> = formulas.iterator()
            if (iterator.hasNext()) {
                var i = 0
                ps.println("INSERT INTO $table ($columns) VALUES")
                ps2.println("INSERT INTO $table2 ($columns2) VALUES")
                while (iterator.hasNext()) {
                    val formula = iterator.next()
                    val isLast = !iterator.hasNext()

                    // formula
                    val row = formula.dataRow()
                    ps.println("($row)${if (isLast) ";" else ","}")

                    // formula args
                    val formulas_args: Collection<Formula_Arg> = make(formula)
                    for (formula_arg in formulas_args) {
                        val row2 = formula_arg.dataRow()
                        val arg = formula_arg.arg
                        val commentArg2 = arg.comment()
                        //val term = formula_arg.getTerm()
                        //val commentTerm2 = term.comment()
                        val commentFormArg2 = formula_arg.comment()
                        ps2.print("${if (i == 0) "" else ",\n"}($row2) /* $commentArg2, $commentFormArg2 */")
                        i++
                    }
                }
                ps2.println(";")
            }
        }

        @Throws(ParseException::class, IOException::class)
        fun insertFormulaArgs(ps: PrintStream, formulas: Iterable<Formula>, table: String, columns: String) {
            val iterator: Iterator<Formula> = formulas.iterator()
            if (iterator.hasNext()) {
                var i = 0
                ps.println("INSERT INTO $table ($columns) VALUES")
                while (iterator.hasNext()) {
                    val formula = iterator.next()

                    // formula args
                    make(formula)
                        .forEach { fa ->
                            val row2 = fa.dataRow()
                            val comment2 = fa.comment()
                            ps.print("${if (i == 0) "" else ",\n"}($row2) /* $comment2 */")
                            i++
                        }
                }
                ps.println(";")
            }
        }

        fun insertSynsets(ps: PrintStream, terms_synsets: Iterable<Term_Synset>, table: String, columns: String) {
            val iterator: Iterator<Term_Synset> = terms_synsets.iterator()
            if (iterator.hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                while (iterator.hasNext()) {
                    val map = iterator.next()
                    val isLast = !iterator.hasNext()

                    val row = map.dataRow()
                    val comment = map.comment()
                    ps.println("($row)${if (isLast) ";" else ","} -- $comment")
                }
            }
        }
    }
}
