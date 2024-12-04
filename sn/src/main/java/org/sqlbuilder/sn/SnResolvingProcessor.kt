package org.sqlbuilder.sn

import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.sn.objects.Collocation
import org.sqlbuilder.sn.objects.Collocation.Companion.parse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

open class SnResolvingProcessor(conf: Properties) : SnProcessor(conf) {

    protected val serFile: String

    @JvmField
    protected val senseResolver: SnSensekeyResolver

    init {
        // header
        this.header += "\n-- " + conf.getProperty("wn_resolve_against")

        // outdir
        this.outDir = File(conf.getProperty("sn_outdir_resolved", "sql/data_resolved"))
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }

        // resolver
        this.resolve = true
        this.serFile = conf.getProperty("sense_nids")
        this.senseResolver = SnSensekeyResolver(this.serFile)
    }

    @Throws(IOException::class)
    override fun run() {
        PrintStream(FileOutputStream(File(outDir, names.file("syntagms"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processSyntagNetFile(ps, File(snHome, snMain), names.table("syntagms"), names.columns("syntagms", true)) { collocation: Collocation, i: Int ->
                val unresolved = collocation.dataRow()
                val r1 = senseResolver.apply(collocation.sensekey1!!) // (word,synsetid)
                val r2 = senseResolver.apply(collocation.sensekey2!!) // (word,synsetid)
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
    }

    @Throws(IOException::class)
    override fun processSyntagNetFile(ps: PrintStream, file: File, table: String, columns: String, consumer: (Collocation, Int) -> Unit) {
        ps.println("INSERT INTO $table ($columns) VALUES")
        process(file, { parse(it) }, consumer)
        ps.print(';')
    }
}
