package org.sqlbuilder.sn

import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.common.Utils.quote
import org.sqlbuilder.sn.objects.Collocation
import org.sqlbuilder.sn.objects.Collocation.Companion.COMPARATOR_BY_SENSEKEYS
import org.sqlbuilder.sn.objects.Collocation.Companion.parse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

class SnUpdatingProcessor(
    conf: Properties,
) : SnResolvingProcessor(conf) {

    init {
        // output
        outDir = File(conf.getProperty("sn_outdir_updated", "sql/data_updated"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        PrintStream(FileOutputStream(File(outDir, names.updateFile("syntagms"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processSyntagNetFile(it, File(snHome, snMain)) { collocation: Collocation, i: Int ->
                updateRow(
                    it,
                    names.table("syntagms"),
                    i,
                    collocation,
                    names.column("syntagms.word1id"),
                    names.column("syntagms.synset1id"),
                    names.column("syntagms.word2id"),
                    names.column("syntagms.synset2id"),
                    names.column("syntagms.sensekey1"),
                    names.column("syntagms.sensekey2")
                )
            }
        }
    }

    private fun updateRow(ps: PrintStream, table: String?, index: Int, collocation: Collocation, vararg columns: String?) {
        val r1 = senseResolver.invoke(collocation.sensekey1!!)
        val r2 = senseResolver.invoke(collocation.sensekey2!!)
        if (r1 != null && r2 != null) {
            val setClause = "${columns[0]}=${nullableInt(r1.first)},${columns[1]}=${nullableInt(r1.second)},${columns[2]}=${nullableInt(r2.first)},${columns[3]}=${nullableInt(r2.second)}"
            val whereClause = "${columns[4]}=${quote(escape(collocation.sensekey1!!))} AND ${columns[5]}=${quote(escape(collocation.sensekey2!!))}"
            ps.println("UPDATE $table SET $setClause WHERE $whereClause; -- ${index + 1}")
        }
    }

    @Throws(IOException::class)
    private fun processSyntagNetFile(ps: PrintStream, file: File, consumer: (Collocation, Int) -> Unit) {
        process(file, COMPARATOR_BY_SENSEKEYS, { parse(it) }, consumer)
    }
}
