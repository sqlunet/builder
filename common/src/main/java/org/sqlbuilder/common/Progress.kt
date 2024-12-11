package org.sqlbuilder.common

import org.sqlbuilder.common.AnsiColors.blue
import org.sqlbuilder.common.AnsiColors.green
import org.sqlbuilder.common.AnsiColors.redb
import java.io.OutputStream
import java.io.PrintStream

object Progress {

    val NONE = PrintStream(object : OutputStream() {
        override fun write(b: Int) {}
    })

    val INFO: PrintStream = System.out

    fun traceSaving(tag: String, message: String? = "") {
        INFO.print(blue("$tag $message"))
    }

    // P R O G R E S S

    val PROGRESS: PrintStream = System.err

    private const val GRANULARITY: Long = 10

    fun traceHeader(tag: String, message: String) {
        PROGRESS.println(">$tag $message")
    }

    fun traceTailer(tag: String, message: String) {
        PROGRESS.println("<$tag $message")
    }

    fun traceTailer(count: Long) {
        PROGRESS.println("\r<$count")
    }

    fun trace(progress: Long) {
        if (progress % GRANULARITY == 0L) {
            var occurs: Long = progress / GRANULARITY
            val c = when (occurs % 4L) {
                0L   -> '—'
                1L   -> '\\'
                2L   -> '|'
                3L   -> '/'
                else -> '.'
            }
            PROGRESS.print("\r$c $progress")
        }
    }

    fun traceDone(message: String? = null) {
        if (message == null) {
            INFO.println(green(" ✓"))
        } else {
            PROGRESS.println(redb(" ✘ $message"))
        }
    }
}
