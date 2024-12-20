package org.semantikos.bnc

import org.semantikos.bnc.objects.BncExtendedRecord
import org.semantikos.bnc.objects.BncRecord
import org.semantikos.common.CommonException
import org.semantikos.common.Logger
import org.semantikos.common.Names
import org.semantikos.common.NotFoundException
import org.semantikos.common.ParseException
import org.semantikos.common.Processor
import org.semantikos.common.Progress
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.Properties

open class BncProcessor(protected val conf: Properties) : Processor("bnc") {

    protected val bncHome: File = File(conf.getProperty("bnc_home", System.getenv()["BNCHOME"]))

    protected val names: Names = Names("bnc")

    protected var header: String = conf.getProperty("bnc_header")

    protected var outDir: File = File(conf.getProperty("bnc_outdir", "sql/data"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    protected open fun processBNCFile(ps: PrintStream, file: File, table: String, columns: String, consumer: (BncRecord, Int) -> Unit) {
        ps.println("INSERT INTO $table ($columns) VALUES")
        process(file, { BncRecord.Companion.parse(it) }, consumer)
        ps.print(';')
    }

    @Throws(IOException::class)
    protected open fun processBNCSubFile(ps: PrintStream, file: File, table: String, columns: String, consumer: (BncRecord, Int) -> Unit) {
        ps.println("INSERT INTO $table ($columns) VALUES")
        process(file, { BncExtendedRecord.Companion.parse(it) }, consumer)
        ps.print(';')
    }

    @Throws(IOException::class)
    protected fun process(file: File, producer: (String) -> BncRecord, consumer: (BncRecord, Int) -> Unit) {
        file.useLines {
            var lineNum = 0
            var count = 0
            it
                .also { ++lineNum }
                .filter { !it.isEmpty() && it[0] == '\t' }
                .map {
                    try {
                        return@map producer.invoke(it)
                    } catch (e: CommonException) {
                        val cause = e.cause
                        if (cause is ParseException) {
                            Logger.Companion.instance.logParseException(BncModule.MODULE_ID, tag, file.name, lineNum.toLong(), it, cause)
                        } /* else if (cause is NotFoundException) {
                            Logger.instance.logNotFoundException(BncModule.MODULE_ID, tag, file.name, lineNum.toLong(), it, cause)
                        }  else if (cause is IgnoreException) {
                            // ignore
                        }
                        */
                    }
                    null
                }
                .filterNotNull()
                .forEach {
                    try {
                        consumer.invoke(it, count.toInt())
                        count++
                    } catch (_: NotFoundException) {
                        // Logger.instance.logNotFoundException(BncModule.MODULE_ID, tag, file.name, lineNum.toLong(), null, nfe)
                    } catch (other: CommonException) {
                        System.err.println(other.cause!!.message)
                    }
                }
        }
    }

    @Throws(IOException::class)
    override fun run() {
        // main
        Progress.traceSaving("bnc", "bncs")
        val bNCMain = conf.getProperty("bnc_main", "bnc.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("bncs"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCFile(it, File(bncHome, bNCMain), names.table("bncs"), names.columns("bncs")) { record: BncRecord, i: Int -> insertRow(it, i.toLong(), record.dataRow()) }
        }
        Progress.traceDone()

        // subfiles
        Progress.traceSaving("bnc", "sp wr")
        val bNCSpWr = conf.getProperty("bnc_spwr", "bnc-spoken-written.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("spwrs"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCSubFile(it, File(bncHome, bNCSpWr), names.table("spwrs"), names.columns("spwrs")) { record: BncRecord, i: Int -> insertRow(it, i.toLong(), record.dataRow()) }
        }
        Progress.traceDone()

        Progress.traceSaving("bnc", "conv task")
        val bNCConvTask = conf.getProperty("bnc_convtask", "bnc-conv-task.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("convtasks"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCSubFile(it, File(bncHome, bNCConvTask), names.table("convtasks"), names.columns("convtasks")) { record: BncRecord, i: Int -> insertRow(it, i.toLong(), record.dataRow()) }
        }
        Progress.traceDone()

        Progress.traceSaving("bnc", "imag inf")
        val bNCImagInf = conf.getProperty("bnc_imaginf", "bnc-imag-inf.txt")
        PrintStream(FileOutputStream(File(outDir, names.file("imaginfs"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processBNCSubFile(it, File(bncHome, bNCImagInf), names.table("imaginfs"), names.columns("imaginfs")) { record: BncRecord, i: Int -> insertRow(it, i.toLong(), record.dataRow()) }
        }
        Progress.traceDone()
    }

    protected fun insertRow(ps: PrintStream, index: Long, values: String) {
        if (index != 0L) {
            ps.print(",\n")
        }
        ps.print("($values)")
    }
}