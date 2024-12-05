package org.sqlbuilder.bnc

import org.sqlbuilder.bnc.objects.BncExtendedRecord
import org.sqlbuilder.bnc.objects.BncRecord
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.quote
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

class BncUpdatingProcessor(conf: Properties) : BncResolvingProcessor(conf) {
    init {
        // output
        outDir = File(conf.getProperty("bnc_outdir_updated", "sql/data_updated"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    private fun processBNCFile(ps: PrintStream, file: File, consumer: (BncRecord, Int) -> Unit) {
        process(file, { BncRecord.Companion.parse(it) }, consumer)
    }

    @Throws(IOException::class)
    private fun processBNCSubFile(ps: PrintStream, file: File, consumer: (BncRecord, Int) -> Unit) {
        process(file, { BncExtendedRecord.Companion.parse(it) }, consumer)
    }

    @Throws(IOException::class)
    override fun run() {
        // main
        val bNCMain = conf.getProperty("bnc_main", "bnc.txt")
        PrintStream(FileOutputStream(File(outDir, names.updateFile("bncs"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCFile(it, File(bncHome, bNCMain), { record: BncRecord, i: Int -> updateRow(it, names.table("bncs"), i, record, names.column("bncs.wordid"), names.column("bncs.word")) })
        }
        // subfiles
        val bNCSpWr = conf.getProperty("bnc_spwr", "bnc-spoken-written.txt")
        PrintStream(FileOutputStream(File(outDir, names.updateFile("spwrs"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCSubFile(it, File(bncHome, bNCSpWr), { record: BncRecord, i: Int -> updateRow(it, names.table("spwrs"), i, record, names.column("spwrs.wordid"), names.column("spwrs.word")) })
        }
        val bNCConvTask = conf.getProperty("bnc_convtask", "bnc-conv-task.txt")
        PrintStream(FileOutputStream(File(outDir, names.updateFile("convtasks"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCSubFile(it, File(bncHome, bNCConvTask), { record: BncRecord, i: Int -> updateRow(it, names.table("convtasks"), i, record, names.column("convtasks.wordid"), names.column("convtasks.word")) })
        }
        val bNCImagInf = conf.getProperty("bnc_imaginf", "bnc-imag-inf.txt")
        PrintStream(FileOutputStream(File(outDir, names.updateFile("imaginfs"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCSubFile(it, File(bncHome, bNCImagInf), { record: BncRecord, i: Int -> updateRow(it, names.table("imaginfs"), i, record, names.column("imaginfs.wordid"), names.column("imaginfs.word")) })
        }
    }

    private fun updateRow(ps: PrintStream, table: String, index: Int, bncRecord: BncRecord, vararg columns: String) {
        val wordid: Int? = wordResolver.apply(bncRecord.word)
        if (wordid != null) {
            val setClause = "${columns[0]}=$wordid"
            val whereClause = "${columns[1]}=${quote(escape(bncRecord.word))}"
            ps.println("UPDATE $table SET $setClause WHERE $whereClause; -- ${index + 1}")
        }
    }
}
