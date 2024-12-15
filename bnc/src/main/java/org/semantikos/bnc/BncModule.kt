package org.semantikos.bnc

import org.semantikos.bnc.BncProcessor
import org.semantikos.bnc.BncResolvingProcessor
import org.semantikos.bnc.BncUpdatingProcessor
import org.semantikos.common.Module
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

        @JvmStatic
        fun main(args: Array<String>) {
            var i = 0
            var mode = Mode.PLAIN
            if (args[i].startsWith("-")) {
                mode = Mode.Companion.read(args[i++])
            }
            val conf = args[i]
            BncModule(conf, mode).run()
        }
    }
}