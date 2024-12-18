package org.semantikos.sn

import org.semantikos.common.*
import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.sn.objects.Collocation
import org.semantikos.sn.objects.Collocation.Companion.COMPARATOR_BY_SENSEKEYS
import org.semantikos.sn.objects.Collocation.Companion.parse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.Throws

open class SnProcessor(
    conf: Properties,
) : Processor("sn") {

    protected val snHome: File = File(conf.getProperty("sn_home", System.getenv()["SNHOME"]))

    protected val snMain: String = conf.getProperty("sn_file", "SYNTAGNET.txt")

    protected val names: Names = Names("sn")

    protected var header: String = conf.getProperty("sn_header")

    protected var resolve: Boolean = false

    protected var outDir: File = File(conf.getProperty("sn_outdir", "sql/data"))

    private var toSenseKeys = SnLemmaPosOffsetResolver(conf.getProperty("to_sensekeys"))

    protected val sensekeyResolver = { lpo: SnLemmaPosOffsetResolvable -> toSenseKeys.invoke(lpo) }

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        traceSaving("sn", "syntagms")
        PrintStream(FileOutputStream(File(outDir, names.file("syntagms"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processSyntagNetFile(it, File(snHome, snMain), names.table("syntagms"), names.columns("syntagms", resolve)) { collocation: Collocation, i: Int -> insertRow(it, i.toLong(), collocation.dataRow()) }
        }
        traceDone()
    }

    @Throws(IOException::class)
    protected open fun processSyntagNetFile(ps: PrintStream, file: File, table: String, columns: String, consumer: (Collocation, Int) -> Unit) {
        ps.println("INSERT INTO $table ($columns) VALUES")
        process(file, COMPARATOR_BY_SENSEKEYS, { line: String -> parse(line) }, consumer)
        ps.print(';')
    }

    @Throws(IOException::class)
    protected fun process(file: File, comparator: Comparator<Collocation>, producer: (String) -> Collocation, consumer: (Collocation, Int) -> Unit) {
        file.useLines { lines ->
            var count0 = 0
            var count1 = 0
            lines
                .filter { !it.isEmpty() && it[0] != '#' }
                .map { line ->
                    try {
                        ++count1
                        return@map producer.invoke(line)
                    } catch (e: CommonException) {
                        val cause = e.cause
                        if (cause is ParseException) {
                            Logger.instance.logParseException(SnModule.MODULE_ID, tag, file.getName(), count1.toLong(), line, cause)
                        } else if (cause is NotFoundException) {
                            Logger.instance.logNotFoundException(SnModule.MODULE_ID, tag, file.getName(), count1.toLong(), line, cause)
                        }
                    }
                    null
                }
                .filterNotNull()
                .filter { it.resolveOffsets(sensekeyResolver) }
                .sortedWith(comparator)
                .forEach {
                    consumer.invoke(it, count0)
                    count0++
                }
        }
    }

    protected fun insertRow(ps: PrintStream, index: Long, values: String) {
        if (index != 0L) {
            ps.print(",\n")
        }
        ps.print("(${index + 1},$values)")
    }
}
