package org.semantikos.sn

import org.semantikos.common.Utils.quote
import org.semantikos.sn.objects.Collocation
import org.semantikos.sn.objects.Collocation.Companion.COMPARATOR_BY_WORDS_POSES
import org.semantikos.sn.objects.Collocation.Companion.parse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

class SnExportingProcessor(
    conf: Properties,
) : SnProcessor(conf) {

    private val outFile = conf.getProperty("sn_outfile_exported") + ".csv"

    init {
        // output
        outDir = File(conf.getProperty("sn_outdir_exported", "data_exported"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

    }

    @Throws(IOException::class)
    override fun run() {
        PrintStream(FileOutputStream(File(outDir, outFile)), true, StandardCharsets.UTF_8).use {
            it.println("# $header")
            processSyntagNetFile(it, File(snHome, snMain)) { collocation: Collocation, i: Int ->
                if (collocation.sensekey1 != collocation.sensekey2)
                    printRow(it, i, collocation)
            }
        }
    }

    private fun printRow(ps: PrintStream, index: Int, collocation: Collocation) {
        val c1 = quote(collocation.sensekey1.toString())
        val c2 = quote(collocation.sensekey2.toString())
        ps.println("$c1,$c2,${index + 1}")
    }

    @Throws(IOException::class)
    private fun processSyntagNetFile(ps: PrintStream, file: File, consumer: (Collocation, Int) -> Unit) {
        process(file, COMPARATOR_BY_WORDS_POSES, { parse(it) }, consumer)
    }
}
