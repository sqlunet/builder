package og.sqlbuilder.sumo

import org.sqlbuilder.su.Kb
import org.sqlbuilder.su.SuModule
import java.io.OutputStream
import java.io.PrintStream

object TestUtils {

    val NULL_OUT: PrintStream = PrintStream(object : OutputStream() {
        override fun write(b: Int) {
            //DO NOTHING
        }
    })

    var OUT: PrintStream = System.out

    val OUT_INFO: PrintStream = System.out

    var OUT_WARN: PrintStream = System.out

    var ERR: PrintStream = System.err

    fun turnOffLogging() {
        SuModule.turnOffLogging()

        val silent = System.getProperties().containsKey("SILENT")
        if (silent) {
            OUT = NULL_OUT
            OUT_WARN = NULL_OUT
        }
    }
}
