package org.semantikos.fn

import org.semantikos.common.Names
import org.semantikos.common.Serialize
import org.semantikos.fn.objects.FE
import org.semantikos.fn.objects.Frame
import org.semantikos.fn.objects.LexUnit
import org.semantikos.fn.objects.Word
import org.semantikos.fn.types.FeType
import org.semantikos.fn.types.FeType.getIntId
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.Throws

class Exporter(
    conf: Properties,
) {

    private val names: Names = Names("fn")

    private val outDir: File = File(conf.getProperty("fn_outdir_ser", "sers"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    fun run() {
        println("frames ${Frame.SET.size}")
        println("fes ${FE.SET.size}")
        println("fetypes ${FeType.COLLECTOR.size}")
        println("lexunits ${LexUnit.SET.size}")
        println("words ${Word.COLLECTOR.size}")

        Word.COLLECTOR.open().use {
            FeType.COLLECTOR.open().use {
                serialize()
                export()
            }
        }
    }

    @Throws(IOException::class)
    fun serialize() {
        serializeFrames()
        serializeFEs()
        serializeLexUnits()
        serializeWords()
    }

    @Throws(IOException::class)
    fun export() {
        exportFrames()
        exportFEs()
        exportLexUnits()
        exportWords()
    }

    @Throws(IOException::class)
    fun serializeFrames() {
        val m = makeFramesMap()
        Serialize.serialize(m, File(outDir, names.serFile("frames.resolve", "_[frame]-[frameid]")))
    }

    @Throws(IOException::class)
    fun serializeFEs() {
        val m = makeFEsMap()
        Serialize.serialize(m, File(outDir, names.serFile("fes.resolve", "_[frame,fetype]-[feid,frameid,fetypeid]")))
    }

    @Throws(IOException::class)
    fun serializeLexUnits() {
        val m = makeLexUnitsMap()
        Serialize.serialize(m, File(outDir, names.serFile("lexunits.resolve", "_[frame,lexunit]-[luid,frameid]")))
    }

    @Throws(IOException::class)
    fun serializeWords() {
        val m = makeWordMap()
        Serialize.serialize(m, File(outDir, names.serFile("words.resolve", "_[word]-[fnwordid]")))
    }

    @Throws(IOException::class)
    fun exportFrames() {
        val m = makeFramesMap().toSortedMap()
        export(m, File(outDir, names.mapFile("frames.resolve", "_[frame]-[frameid]")))
    }

    @Throws(IOException::class)
    fun exportFEs() {
        val m = makeFEsMap().toSortedMap(STRING_PAIR_COMPARATOR)
        export(m, File(outDir, names.mapFile("fes.resolve", "_[frame,fetype]-[feid,frameid,fetypeid]")))
    }

    @Throws(IOException::class)
    fun exportLexUnits() {
        val m = makeLexUnitsMap().toSortedMap(STRING_PAIR_COMPARATOR)
        export(m, File(outDir, names.mapFile("lexunits.resolve", "_[frame,lexunit]-[luid,frameid]")))
    }

    @Throws(IOException::class)
    fun exportWords() {
        val m = makeWordMap().toSortedMap()
        export(m, File(outDir, names.mapFile("words.resolve", "_[word]-[fnwordid]")))
    }

    // M A P S

    /**
     * Make word to wordid map
     *
     * @return word to wordid
     */
    fun makeWordMap(): Map<String, Int> {
        return Word.COLLECTOR.iterator()
            .asSequence()
            .map { it.word to Word.COLLECTOR.invoke(it) }
            .toMap()
    }

    fun makeFramesMap(): Map<String, Int> {
        return Frame.SET
            .asSequence()
            .map { it.name to it.iD }
            .toMap()
    }

    fun makeFEsMap(): Map<Pair<String, String>, Triple<Int, Int, Int>> {
        val id2frame = Frame.SET
            .asSequence()
            .map { it.iD to it.name }
            .toMap()
        return FE.SET
            .asSequence()
            .map { Pair(id2frame[it.frameID]!!, it.name) to Triple(it.iD, it.frameID, getIntId(it.name)!!) }
            .toMap()
    }

    fun makeLexUnitsMap(): Map<Pair<String, String>, Pair<Int, Int>> {
        return LexUnit.SET
            .asSequence()
            .map { Pair(it.frameName, it.name) to Pair(it.iD, it.frameID) }
            .toMap()
    }

    companion object {

        private val STRING_PAIR_COMPARATOR: Comparator<Pair<String, String>> = Comparator
            .comparing<Pair<String, String>, String> { it.first }
            .thenComparing<String> { it.second }

        @Throws(IOException::class)
        fun <K, V> export(m: Map<K, V>, file: File) {
            PrintStream(FileOutputStream(file), true, StandardCharsets.UTF_8).use { ps ->
                export(ps, m)
            }
        }

        fun <K, V> export(ps: PrintStream, m: Map<K, V>) {
            m.forEach { ps.println("${it.key} -> ${it.value}") }
        }
    }
}
