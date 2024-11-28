package org.sqlbuilder.bnc

import org.sqlbuilder.bnc.objects.BncExtendedRecord
import org.sqlbuilder.bnc.objects.BncRecord
import org.sqlbuilder.common.NotFoundException
import org.sqlbuilder.common.ThrowingBiConsumer
import org.sqlbuilder.common.ThrowingFunction
import org.sqlbuilder.common.Utils.nullableInt
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

open class BncResolvingProcessor(conf: Properties) : BncProcessor(conf) {

    protected val serFile: String

    @JvmField
    protected val wordResolver: BncWordResolver

    init {
        // header
        this.header += "\n-- " + conf.getProperty("wn_resolve_against")

        // output
        this.outDir = File(conf.getProperty("bnc_outdir_resolved", "sql/data_resolved"))
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }

        // resolve
        this.serFile = conf.getProperty("word_nids")
        this.wordResolver = BncWordResolver(this.serFile)
    }

    @Throws(IOException::class)
    override fun run() {
        // main
        val bNCMain = conf.getProperty("bnc_main", "bnc.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("bncs"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processBNCFile(ps, File(bncHome, bNCMain), names.table("bncs"), names.columns("bncs", true), ThrowingBiConsumer { record: BncRecord, i: Int -> resolveAndInsert(ps, record, i) })
        }
        // subfiles
        val bNCSpWr = conf.getProperty("bnc_spwr", "bnc-spoken-written.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("spwrs"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processBNCSubFile(ps, File(bncHome, bNCSpWr), names.table("spwrs"), names.columns("spwrs", true), ThrowingBiConsumer { record: BncRecord, i: Int -> resolveAndInsert(ps, record, i) })
        }
        val bNCConvTask = conf.getProperty("bnc_convtask", "bnc-conv-task.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("convtasks"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processBNCSubFile(ps, File(bncHome, bNCConvTask), names.table("convtasks"), names.columns("convtasks", true), ThrowingBiConsumer { record: BncRecord, i: Int -> resolveAndInsert(ps, record, i) })
        }
        val bNCImagInf = conf.getProperty("bnc_imaginf", "bnc-imag-inf.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("imaginfs"))), true, StandardCharsets.UTF_8).use { ps ->
            ps.println("-- $header")
            processBNCSubFile(ps, File(bncHome, bNCImagInf), names.table("imaginfs"), names.columns("imaginfs", true), ThrowingBiConsumer { record: BncRecord, i: Int -> resolveAndInsert(ps, record, i) })
        }
    }

    @Throws(NotFoundException::class)
    private fun resolveAndInsert(ps: PrintStream, record: BncRecord, i: Int) {
        val nr = record.dataRow()
        val r: Int = wordResolver.apply(record.word)
        val values = String.format("%s,%s", nr, nullableInt(r))
        insertRow(ps, i.toLong(), values)
        return
    }

    @Throws(IOException::class)
    override fun processBNCFile(ps: PrintStream, file: File, table: String, columns: String, consumer: ThrowingBiConsumer<BncRecord, Int>) {
        ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
        process(file, ThrowingFunction { BncRecord.Companion.parse(it) }, consumer)
        ps.print(';')
    }

    @Throws(IOException::class)
    override fun processBNCSubFile(ps: PrintStream, file: File, table: String, columns: String, consumer: ThrowingBiConsumer<BncRecord, Int>) {
        ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
        process(file, ThrowingFunction { BncExtendedRecord.Companion.parse(it) }, consumer)
        ps.print(';')
    }
}
