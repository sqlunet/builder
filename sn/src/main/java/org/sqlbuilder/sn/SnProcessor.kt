package org.sqlbuilder.sn

import org.sqlbuilder.common.*
import org.sqlbuilder.sn.objects.Collocation
import org.sqlbuilder.sn.objects.Collocation.Companion.COMPARATOR
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.BiConsumer

open class SnProcessor(
    conf: Properties,
) : Processor("sn") {

    @JvmField
    protected val snHome: File = File(conf.getProperty("sn_home", System.getenv()["SNHOME"]))

    @JvmField
    protected val snMain: String = conf.getProperty("sn_file", "SYNTAGNET.txt")

    @JvmField
    protected val names: Names = Names("sn")

    @JvmField
    protected var header: String = conf.getProperty("sn_header")

    @JvmField
    protected var resolve: Boolean = false

    @JvmField
    protected var outDir: File = File(conf.getProperty("sn_outdir", "sql/data"))

    private var toSenseKeys = SnLemmaPosOffsetResolver(conf.getProperty("to_sensekeys"))

    protected val sensekeyResolver = { lpo: SnLemmaPosOffsetResolvable -> toSenseKeys.apply(lpo) }

    init {
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        PrintStream(FileOutputStream(File(outDir, names.file("syntagms"))), true, StandardCharsets.UTF_8).use {
            it.println("-- $header")
            processSyntagNetFile(it, File(snHome, snMain), names.table("syntagms"), names.columns("syntagms", resolve)) { collocation: Collocation, i: Int -> insertRow(it, i.toLong(), collocation.dataRow()) }
        }
    }

    @Throws(IOException::class)
    protected open fun processSyntagNetFile(ps: PrintStream, file: File, table: String, columns: String, consumer: (Collocation, Int) -> Unit) {
        ps.println("INSERT INTO $table ($columns)")
        process(file, { line: String -> Collocation.Companion.parse(line) }, consumer)
        ps.print(';')
    }

    @Throws(IOException::class)
    protected fun process(file: File, producer: (String) -> Collocation, consumer: BiConsumer<Collocation, Int>) {
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
                .sortedWith(COMPARATOR)
                .forEach {
                    consumer.accept(it, count0)
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
