package org.semantikos.bnc

import org.semantikos.bnc.objects.BncExtendedRecord
import org.semantikos.bnc.objects.BncRecord
import org.semantikos.bnc.BncWordResolver
import org.semantikos.common.NotFoundException
import org.semantikos.common.Progress
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.Properties

open class BncResolvingProcessor(conf: Properties) : BncProcessor(conf) {

    protected val serFile: String

    protected val wordResolver: BncWordResolver

    init {
        // header
        header += "\n-- ${conf.getProperty("wn_resolve_against")}"

        // output
        outDir = File(conf.getProperty("bnc_outdir_resolved", "sql/data_resolved"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        // resolve
        serFile = conf.getProperty("word_nids")
        wordResolver = BncWordResolver(serFile)
    }

    @Throws(IOException::class)
    override fun processBNCFile(ps: PrintStream, file: File, table: String, columns: String, consumer: (BncRecord, Int) -> Unit) {
        ps.println("INSERT INTO $table ($columns) VALUES")
        process(file, { BncRecord.Companion.parse(it) }, consumer)
        ps.print(';')
    }

    @Throws(IOException::class)
    override fun processBNCSubFile(ps: PrintStream, file: File, table: String, columns: String, consumer: (BncRecord, Int) -> Unit) {
        ps.println("INSERT INTO $table ($columns) VALUES")
        process(file, { BncExtendedRecord.Companion.parse(it) }, consumer)
        ps.print(';')
    }

    @Throws(IOException::class)
    override fun run() {
        // main
        Progress.traceSaving("bnc", "bncs")
        val bNCMain = conf.getProperty("bnc_main", "bnc.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("bncs"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCFile(it, File(bncHome, bNCMain), names.table("bncs"), names.columns("bncs", true)) { record: BncRecord, i: Int -> resolveAndInsert(it, record, i) }
        }
        Progress.traceDone()

        // subfiles
        Progress.traceSaving("bnc", "sp wr")
        val bNCSpWr = conf.getProperty("bnc_spwr", "bnc-spoken-written.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("spwrs"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCSubFile(it, File(bncHome, bNCSpWr), names.table("spwrs"), names.columns("spwrs", true)) { record: BncRecord, i: Int -> resolveAndInsert(it, record, i) }
        }
        Progress.traceDone()

        Progress.traceSaving("bnc", "conv task")
        val bNCConvTask = conf.getProperty("bnc_convtask", "bnc-conv-task.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("convtasks"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCSubFile(it, File(bncHome, bNCConvTask), names.table("convtasks"), names.columns("convtasks", true)) { record: BncRecord, i: Int -> resolveAndInsert(it, record, i) }
        }
        Progress.traceDone()

        Progress.traceSaving("bnc", "imag inf")
        val bNCImagInf = conf.getProperty("bnc_imaginf", "bnc-imag-inf.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("imaginfs"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCSubFile(it, File(bncHome, bNCImagInf), names.table("imaginfs"), names.columns("imaginfs", true)) { record: BncRecord, i: Int -> resolveAndInsert(it, record, i) }
        }
        Progress.traceDone()
    }

    @Throws(NotFoundException::class)
    private fun resolveAndInsert(ps: PrintStream, record: BncRecord, i: Int) {
        val nr = record.dataRow()
        val r: Int? = wordResolver.invoke(record.word)
        if (r != null) {
            val values = "$nr,$r"
            insertRow(ps, i.toLong(), values)
            return
        }
        throw NotFoundException(record.word)
    }
}