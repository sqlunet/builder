package org.sqlbuilder.bnc

import org.sqlbuilder.bnc.objects.BncExtendedRecord
import org.sqlbuilder.bnc.objects.BncRecord
import org.sqlbuilder.common.ThrowingBiConsumer
import org.sqlbuilder.common.ThrowingFunction
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
        this.outDir = File(conf.getProperty("bnc_outdir_updated", "sql/data_updated"))
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        // main
        val bNCMain = conf.getProperty("bnc_main", "bnc.txt")
        PrintStream(FileOutputStream(File(outDir, names.updateFile("bncs"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processBNCFile(ps, File(bncHome, bNCMain), ThrowingBiConsumer { record: BncRecord, i: Int -> updateRow(ps, names.table("bncs"), i, record, names.column("bncs.wordid"), names.column("bncs.word")) })
        }
        // subfiles
        val bNCSpWr = conf.getProperty("bnc_spwr", "bnc-spoken-written.txt")
        PrintStream(FileOutputStream(File(outDir, names.updateFile("spwrs"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processBNCSubFile(ps, File(bncHome, bNCSpWr), ThrowingBiConsumer { record: BncRecord, i: Int -> updateRow(ps, names.table("spwrs"), i, record, names.column("spwrs.wordid"), names.column("spwrs.word")) })
        }
        val bNCConvTask = conf.getProperty("bnc_convtask", "bnc-conv-task.txt")
        PrintStream(FileOutputStream(File(outDir, names.updateFile("convtasks"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processBNCSubFile(ps, File(bncHome, bNCConvTask), ThrowingBiConsumer { record: BncRecord, i: Int -> updateRow(ps, names.table("convtasks"), i, record, names.column("convtasks.wordid"), names.column("convtasks.word")) })
        }
        val bNCImagInf = conf.getProperty("bnc_imaginf", "bnc-imag-inf.txt")
        PrintStream(FileOutputStream(File(outDir, names.updateFile("imaginfs"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processBNCSubFile(ps, File(bncHome, bNCImagInf), ThrowingBiConsumer { record: BncRecord, i: Int -> updateRow(ps, names.table("imaginfs"), i, record, names.column("imaginfs.wordid"), names.column("imaginfs.word")) })
        }
    }

    @Throws(IOException::class)
    private fun processBNCFile(ps: PrintStream, file: File, consumer: ThrowingBiConsumer<BncRecord, Int>) {
        process(file, ThrowingFunction { BncRecord.Companion.parse(it) }, consumer)
    }

    @Throws(IOException::class)
    private fun processBNCSubFile(ps: PrintStream, file: File, consumer: ThrowingBiConsumer<BncRecord, Int>) {
        process(file, ThrowingFunction { BncExtendedRecord.Companion.parse(it) }, consumer)
    }

    private fun updateRow(ps: PrintStream, table: String, index: Int, bncRecord: BncRecord, vararg columns: String) {
        val wordid: Int = wordResolver.apply(bncRecord.word)
        val setClause = String.format("%s=%d", columns[0], wordid)
        val whereClause = String.format("%s=%s", columns[1], quote(escape(bncRecord.word)))
        ps.printf("UPDATE %s SET %s WHERE %s; -- %d%n", table, setClause, whereClause, index + 1)
    }
}
