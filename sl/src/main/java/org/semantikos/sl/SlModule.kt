package org.semantikos.sl

import org.semantikos.common.Module
import org.semantikos.common.Module.Mode.Companion.read
import org.semantikos.sl.collectors.SemlinkProcessor
import org.semantikos.sl.collectors.SemlinkUpdatingProcessor
import java.io.IOException

class SlModule(
    conf: String,
    mode: Mode
) : Module(MODULE_ID, conf, mode) {

    override fun run() {
        checkNotNull(props)

        when (mode) {
            Mode.PLAIN, Mode.RESOLVE -> {
                SemlinkProcessor(props).run()
                try {
                    val inserter = if (mode == Mode.PLAIN) Inserter(props) else ResolvingInserter(props)
                    inserter.insert()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }

            Mode.UPDATE              -> {
                SemlinkUpdatingProcessor(props).run()
                try {
                    val inserter: Inserter = ResolvingUpdater(props)
                    inserter.insert()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }

            Mode.EXPORT              -> {}
            else                     -> {}
        }
    }

    companion object {

        const val MODULE_ID: String = "sl"

        @JvmStatic
        fun main(args: Array<String>) {
            var i = 0
            var mode = Mode.PLAIN
            if (args[i].startsWith("-")) {
                mode = read(args[i++])
            }
            val conf = args[i]
            SlModule(conf, mode).run()
        }
    }
}
