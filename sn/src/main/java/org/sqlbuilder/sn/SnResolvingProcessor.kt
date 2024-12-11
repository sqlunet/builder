package org.sqlbuilder.sn

import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.traceSaving
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.sn.objects.Collocation
import org.sqlbuilder.sn.objects.Collocation.Companion.COMPARATOR_BY_SENSEKEYS
import org.sqlbuilder.sn.objects.Collocation.Companion.parse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

open class SnResolvingProcessor(conf: Properties) : SnProcessor(conf) {

    protected val serFile: String

    protected val senseResolver: SnSensekeyResolver

    init {
        // header
        header += "\n-- " + conf.getProperty("wn_resolve_against")

        // outdir
        outDir = File(conf.getProperty("sn_outdir_resolved", "sql/data_resolved"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        // resolver
        resolve = true
        serFile = conf.getProperty("sense_nids")
        senseResolver = SnSensekeyResolver(serFile)
    }

    @Throws(IOException::class)
    override fun run() {
        traceSaving("sn", "syntagms")
        PrintStream(FileOutputStream(File(outDir, names.file("syntagms"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processSyntagNetFile(ps, File(snHome, snMain), names.table("syntagms"), names.columns("syntagms", true)) { collocation: Collocation, i: Int ->
                val unresolved = collocation.dataRow()
                val r1 = senseResolver.invoke(collocation.sensekey1!!) // (word,synsetid)
                val r2 = senseResolver.invoke(collocation.sensekey2!!) // (word,synsetid)
                if (r1 != null && r2 != null) {
                    val word1nid = nullableInt(r1.first)
                    val synset1nid = nullableInt(r1.second)
                    val word2nid = nullableInt(r2.first)
                    val synset2nid = nullableInt(r2.second)
                    val values = "$unresolved,$word1nid,$synset1nid,$word2nid,$synset2nid"
                    insertRow(ps, i.toLong(), values)
                }
            }
        }
        traceDone()
    }

    @Throws(IOException::class)
    override fun processSyntagNetFile(ps: PrintStream, file: File, table: String, columns: String, consumer: (Collocation, Int) -> Unit) {
        ps.println("INSERT INTO $table ($columns) VALUES")
        process(file, COMPARATOR_BY_SENSEKEYS, { parse(it) }, consumer)
        ps.print(';')
    }
}
