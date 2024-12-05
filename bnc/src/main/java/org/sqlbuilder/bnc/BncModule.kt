package org.sqlbuilder.bnc

import org.sqlbuilder.common.Module
import org.sqlbuilder.common.Module.Mode.Companion.read
import java.io.IOException

class BncModule(
    conf: String,
    mode: Mode,
) : Module(MODULE_ID, conf, mode) {

    override fun run() {
        checkNotNull(props)
        try {
            when (mode) {
                Mode.PLAIN   -> BncProcessor(props).run()
                Mode.RESOLVE -> BncResolvingProcessor(props).run()
                Mode.UPDATE  -> BncUpdatingProcessor(props).run()
                else         -> {}
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    companion object {

        const val MODULE_ID: String = "bnc"

        fun main(args: Array<String>) {
            var i = 0
            var mode = Mode.PLAIN
            if (args[i].startsWith("-")) {
                mode = read(args[i++])
            }
            val conf = args[i]
            BncModule(conf, mode).run()
        }
    }
}
