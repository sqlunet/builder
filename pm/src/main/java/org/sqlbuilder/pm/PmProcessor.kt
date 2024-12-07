package org.sqlbuilder.pm

import org.sqlbuilder.common.*
import org.sqlbuilder.pm.objects.PmEntry
import org.sqlbuilder.pm.objects.PmEntry.Companion.parse
import org.sqlbuilder.pm.objects.PmPredicate
import org.sqlbuilder.pm.objects.PmRole
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.Throws

open class PmProcessor(conf: Properties) : Processor("pm") {

    protected val pMHome: String = conf.getProperty("pm_home", System.getenv()["PMHOME"])

    protected val pMFile: String = conf.getProperty("pm_file", System.getenv()["PredicateMatrix.txt"])

    protected open val names: Names = Names("pm")

    protected var outDir: File = File(conf.getProperty("pm_outdir", "sql/data"))

    protected var header: String = conf.getProperty("pm_header")

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        val inputFile = File(pMHome, pMFile)
        process<PmRole>(inputFile, { PmRole.Companion.parse(it) }, null)

        PmPredicate.COLLECTOR.open().use {
            Insert.insert<PmPredicate>(PmPredicate.COLLECTOR, PmPredicate.COLLECTOR, File(outDir, names.file("predicates")), names.table("predicates"), names.columns("predicates"), header)
            PmRole.COLLECTOR.open().use {
                Insert.insert<PmRole>(PmRole.COLLECTOR, PmRole.COLLECTOR, File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"), header)
                PrintStream(FileOutputStream(File(outDir, names.file("pms"))), true, StandardCharsets.UTF_8).use {
                    it.println("-- $header")
                    processPmFile(it, inputFile, names.table("pms"), names.columns("pms", false)) { role, i -> insertRow(it, i, role.dataRow()) }
                }
            }
        }
    }

    @Throws(IOException::class)
    protected fun processPmFile(ps: PrintStream, file: File, table: String, columns: String, consumer: (PmEntry, Int) -> Unit) {
        ps.println("INSERT INTO $table ($columns) VALUES")
        process(file, { parse(it) }, consumer)
        ps.print(';')
    }

    protected fun insertRow(ps: PrintStream, index: Int, values: String) {
        if (index != 0) {
            ps.print(",\n")
        }
        ps.print("(${index + 1},$values)")
    }

    companion object {

        @Throws(IOException::class)
        fun <T> process(file: File, producer: (String) -> T, consumer: ((T, Int) -> Unit)?) {
            file.useLines {
                var count = 0
                var lineNo = 0
                it
                    .also { ++lineNo }
                    .filter { !it.isEmpty() && it[0] != '\t' }
                    .map { line ->
                        try {
                            return@map producer.invoke(line)
                        } catch (e: CommonException) {
                            val cause = e.cause
                            if (cause is ParseException) {
                                Logger.instance.logParseException(PmModule.MODULE_ID, "pm", file.getName(), lineNo.toLong(), line, cause)
                            } else if (cause is NotFoundException) {
                                Logger.instance.logNotFoundException(PmModule.MODULE_ID, "pm", file.getName(), lineNo.toLong(), line, cause)
                            }
                        }
                        null
                    }
                    .filterNotNull()
                    .forEach {
                        consumer?.invoke(it, count)
                        count++
                    }
            }
        }
    }
}
