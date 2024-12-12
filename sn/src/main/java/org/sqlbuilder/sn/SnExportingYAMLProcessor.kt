package org.sqlbuilder.sn

import org.sqlbuilder.common.Utils.quote
import org.sqlbuilder.sn.objects.Collocation
import org.sqlbuilder.sn.objects.Collocation.Companion.COMPARATOR_BY_WORDS_POSES
import org.sqlbuilder.sn.objects.Collocation.Companion.parse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.Throws

class SnExportingYAMLProcessor(
    conf: Properties,
) : SnProcessor(conf) {

    private val outFile = conf.getProperty("sn_outfile_exported") + ".yaml"

    init {
        // output
        outDir = File(conf.getProperty("sn_outdir_exported", "data_exported"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

    }

    @Throws(IOException::class)
    override fun run() {
        PrintStream(FileOutputStream(File(outDir, outFile)), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("# $header")
            val accumulator = ArrayList<Pair<String, String>>()
            processSyntagNetFile(File(snHome, snMain)) { collocation: Collocation, i: Int ->
                accumulator.add(collocation.sensekey1.toString() to collocation.sensekey2.toString())
            }
            accumulator
                .asSequence()
                .groupBy({ it.first }, { it.second })
                .toMap()
                .toSortedMap()
                .forEach { k, vs ->
                    ps.println("${quote(k)}:")
                    ps.println("${indent}collocation:")
                    vs.forEach {
                        ps.println("${indent}${indent}-${quote(it)}")
                    }
                }
        }
    }

    @Throws(IOException::class)
    private fun processSyntagNetFile(file: File, consumer: (Collocation, Int) -> Unit) {
        process(file, COMPARATOR_BY_WORDS_POSES, { parse(it) }, consumer)
    }

    companion object
    const

    val indent = "  "
}
