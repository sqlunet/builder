package org.sqlbuilder2.legacy

import org.sqlbuilder.common.Logger
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.ParseException
import org.sqlbuilder.common.Processor
import org.sqlbuilder2.ser.Serialize
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.Throws

class SynsetToSynsetProcessor(private val conf: Properties) : Processor("sy2sy") {

    private val names: Names = Names("legacy")

    private val from: String = conf.getProperty("from")

    private val to: String = conf.getProperty("to", "XX")

    private val inDir: File = File(conf.getProperty("synsets_to_synsets.sourcedir"))

    private val outDir: File = File(conf.getProperty("synsets_to_synsets.destdir", "mappings"))

    init {
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        runSer()
        runSql()
    }

    @Throws(IOException::class)
    private fun runSer() {
        val mappings = conf.getProperty("synsets_to_synsets.sourcefile").replace("\\$\\{from}".toRegex(), from).replace("\\$\\{to}".toRegex(), to)
        val outFile = names.file("synsets_to_synsets").replace("\\$\\{from}".toRegex(), from).replace("\\$\\{to}".toRegex(), if ("XX" == to) "" else to) + ".ser"

        val allMaps: MutableMap<Char, Map<Long, Long>> = HashMap<Char, Map<Long, Long>>()

        val legacyNoun = mappings.replace("\\$\\{pos}".toRegex(), "noun")
        val nMap = processSerSynsetToSynsetFile(File(inDir, legacyNoun))
        allMaps.put('n', nMap)

        val legacyVerb = mappings.replace("\\$\\{pos}".toRegex(), "verb")
        val vMap = processSerSynsetToSynsetFile(File(inDir, legacyVerb))
        allMaps.put('v', vMap)

        val legacyAdj = mappings.replace("\\$\\{pos}".toRegex(), "adj")
        val aMap = processSerSynsetToSynsetFile(File(inDir, legacyAdj))
        allMaps.put('a', aMap)

        val legacyAdv = mappings.replace("\\$\\{pos}".toRegex(), "adv")
        val rMap = processSerSynsetToSynsetFile(File(inDir, legacyAdv))
        allMaps.put('r', rMap)

        Serialize.serialize(allMaps, File(outDir, outFile))
    }

    @Throws(IOException::class)
    private fun processSerSynsetToSynsetFile(file: File): Map<Long, Long> {
        file.useLines {
            val count = 0L
            return it
                .asSequence()
                .also { count }
                .filter { !it.isEmpty() && it[0] != '#' }
                .map {
                    try {
                        val m = SynsetToSynsetMapping.parse(it)
                        return@map m.from to m.to
                    } catch (pe: ParseException) {
                        Logger.instance.logParseException(LegacyModule.MODULE_ID, tag, file.getName(), count, it, pe)
                    }
                    null
                }
                .filterNotNull()
                .toMap()
        }
    }

    @Throws(IOException::class)
    private fun runSql() {
        val mappings = conf.getProperty("synsets_to_synsets.sourcefile").replace("\\$\\{from}".toRegex(), from).replace("\\$\\{to}".toRegex(), to)
        val outFile = names.file("synsets_to_synsets").replace("\\$\\{from}".toRegex(), from).replace("\\$\\{to}".toRegex(), if ("XX" == to) "" else to) + ".sql"

        val table = names.table("synsets_to_synsets").replace("\\$\\{from}".toRegex(), from).replace("\\$\\{to}".toRegex(), if ("XX" == to) "" else to)
        val columns = names.columns("synsets_to_synsets")
        PrintStream(FileOutputStream(File(outDir, outFile)), true, StandardCharsets.UTF_8).use { ps ->
            val legacyNoun = mappings.replace("\\$\\{pos}".toRegex(), "noun")
            processSqlSynsetToSynsetFile(ps, File(inDir, legacyNoun), table, columns, 'n')
            ps.println()

            val legacyVerb = mappings.replace("\\$\\{pos}".toRegex(), "verb")
            processSqlSynsetToSynsetFile(ps, File(inDir, legacyVerb), table, columns, 'v')
            ps.println()

            val legacyAdj = mappings.replace("\\$\\{pos}".toRegex(), "adj")
            processSqlSynsetToSynsetFile(ps, File(inDir, legacyAdj), table, columns, 'a')
            ps.println()

            val legacyAdv = mappings.replace("\\$\\{pos}".toRegex(), "adv")
            processSqlSynsetToSynsetFile(ps, File(inDir, legacyAdv), table, columns, 'r')
        }
    }

    @Throws(IOException::class)
    private fun processSqlSynsetToSynsetFile(ps: PrintStream, file: File, table: String, columns: String, pos: Char) {
        ps.printf("-- %s%n", names.header("synsets_to_synsets").replace("\\$\\{from}".toRegex(), from).replace("\\$\\{to}".toRegex(), to))
        ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
        file.useLines {
            var count = 0L
            var count1 = 0L
            it
                .asSequence()
                .also { count1++ }
                .filter { !it.isEmpty() && it[0] != '#' }
                .map {
                    try {
                        return@map SynsetToSynsetMapping.parse(it)
                    } catch (pe: ParseException) {
                        Logger.instance.logParseException(LegacyModule.MODULE_ID, tag, file.getName(), count, it, pe)
                    }
                    null
                }  
                .filterNotNull()
                .forEach {
                    val values = it.dataRow()
                    insertRow(ps, count, pos, values)
                    count++
                }
        }
        ps.print(';')
    }

    private fun insertRow(ps: PrintStream, index: Long, pos: Char, values: String) {
        if (index != 0L) {
            ps.print(",\n")
        }
        ps.print("('$pos',$values)")
    }
}
