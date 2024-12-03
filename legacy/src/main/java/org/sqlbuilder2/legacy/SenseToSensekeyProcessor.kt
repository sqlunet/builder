package org.sqlbuilder2.legacy

import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Processor
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder2.ser.Serialize
import org.sqlbuilder2.ser.Triplet
import java.io.File
import java.io.IOException
import java.util.*
import java.util.function.BiFunction
import kotlin.Throws

/**
 * Index sense
 * From line in index.sense
 * To triplet(lemma,pos,offset)-to-sensekey map
 */
class SenseToSensekeyProcessor(private val conf: Properties) : Processor("sk2nid") {

    private val names: Names = Names("legacy")

    private val from: String = conf.getProperty("from")

    private val inDir: File = File(conf.getProperty("senses_to_sensekeys.sourcedir").replace("\\$\\{from}".toRegex(), from))

    private val outDir: File = File(conf.getProperty("senses_to_sensekeys.destdir", "mappings"))

    @Throws(IOException::class)
    override fun run() {
        process()
    }

    @Throws(IOException::class)
    fun process() {
        val inFile = conf.getProperty("senses_to_sensekeys.sourcefile").replace("\\$\\{from}".toRegex(), from)
        val outFile = names.file("senses_to_sensekeys").replace("\\$\\{from}".toRegex(), from)

        val m: Map<Triplet<String, Char, Int>, String> = getLemmaPosOffsetToSensekey(File(inDir, inFile))
        Serialize.serialize(m, File(outDir, "$outFile.ser"))

        val m2: Map<Triplet<String, Char, Int>, String> = getLemmaPosOffsetToSensekeyOrdered(File(inDir, inFile))
        insert<Triplet<String, Char, Int>, String>(
            m2.keys,
            { key: Triplet<String, Char, Int> -> m2[key]!! },
            File(outDir, "$outFile.sql"), names.table("senses_to_sensekeys").replace("\\$\\{from}".toRegex(), from),
            names.columns("senses_to_sensekeys"),
            names.header("senses_to_sensekeys").replace("\\$\\{from}".toRegex(), from),
            BiFunction { k: Triplet<String, Char, Int>, v: String -> "'${escape(k.first)}','${k.second}',${k.third},'${escape(v)}'" })
    }

    init {
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    companion object {

        /*
         abandon%2:31:00:: 00614057 5 3
         abandon%2:31:01:: 00613393 4 5
         abandon%2:38:00:: 02076676 3 6
         abandon%2:40:00:: 02228031 1 10
         abandon%2:40:01:: 02227741 2 6
         */

        @JvmStatic
        @Throws(IOException::class)
        fun getLemmaPosOffsetToSensekey(file: File): Map<Triplet<String, Char, Int>, String> {
            file.useLines {
                return it
                    .filter { !it.isEmpty() && it[0] != '#' }
                    .map { it.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() }
                    .map { Triplet(getLemmaFromSensekey(it[0]), getPosFromSensekey(it[0]), it[1].toInt()) to it[0] }
                    .toMap()
            }
        }

        var tripletComparator: Comparator<Triplet<String, Char, Int>> = Comparator
            .comparing<Triplet<String, Char, Int>, String> { it.first }
            .thenComparing<Char> { it.second }
            .thenComparing<Int> { it.third }

        @Throws(IOException::class)
        fun getLemmaPosOffsetToSensekeyOrdered(file: File): Map<Triplet<String, Char, Int>, String> {
            file.useLines {
                return it
                    .filter { !it.isEmpty() && it[0] != '#' }
                    .map { it.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() }
                    .map { Triplet(getLemmaFromSensekey(it[0]), getPosFromSensekey(it[0]), it[1].toInt()) to it[0] }
                    .toMap()
                    .toSortedMap(tripletComparator)
            }
        }

        @Throws(IOException::class)
        fun getSensekeyToOffset(file: File): Map<String, Int> {
            file.useLines {
                return it
                    .filter { !it.isEmpty() && it[0] != '#' }
                    .map { it.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() }
                    .map { it[0] to it[1].toInt() }
                    .toMap()
            }
        }

        private fun getPosFromSensekey(sensekey: String): Char {
            val b = sensekey.indexOf('%')
            val c = sensekey[b + 1]
            return when (c) {
                '1' -> 'n'
                '2' -> 'v'
                '3', '5' -> 'a'
                '4' -> 'r'
                else -> throw IllegalArgumentException(sensekey)
            }
        }

        private fun getLemmaFromSensekey(sensekey: String): String {
            val b = sensekey.indexOf('%')
            return sensekey.substring(0, b).replace('_', ' ')
        }
    }
}
