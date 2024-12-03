package org.sqlbuilder.pm

import org.sqlbuilder.common.Module
import org.sqlbuilder.common.Module.Mode.Companion.read
import java.io.IOException

class PmModule(
    conf: String, mode: Mode
) : Module(MODULE_ID, conf, mode) {

    override fun run() {
        checkNotNull(props)

        try {
            when (mode) {
                Mode.PLAIN   -> PmProcessor(props).run()
                Mode.RESOLVE -> PmResolvingProcessor(props).run()
                Mode.UPDATE  -> PmUpdatingProcessor(props).run()
                Mode.EXPORT  -> {}
                else         -> {}
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    companion object {

        const val MODULE_ID: String = "pm"

        @JvmStatic
        fun main(args: Array<String>) {
            var i = 0
            var mode = Mode.PLAIN
            if (args[i].startsWith("-")) {
                mode = read(args[i++])
            }
            val conf = args[i]
            PmModule(conf, mode).run()
        }
    }
}
