package org.semantikos.pb

import org.semantikos.common.Module
import org.semantikos.pb.collectors.PbCollector
import org.semantikos.pb.collectors.PbExportCollector
import org.semantikos.pb.collectors.PbUpdateCollector
import java.io.IOException

open class PbModule protected constructor(conf: String, mode: Mode) : Module(MODULE_ID, conf, mode) {

    override fun run() {
        checkNotNull(props)

        when (mode) {
            Mode.PLAIN, Mode.RESOLVE -> {
                PbCollector(props).run()
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
                PbUpdateCollector(props).run()
                try {
                    val inserter: Inserter = ResolvingUpdater(props)
                    inserter.insert()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }

            Mode.EXPORT              -> {
                PbExportCollector(props).run()
                try {
                    val exporter = Exporter(props)
                    exporter.run()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            else                     -> {}
        }
    }

    companion object {

        const val MODULE_ID: String = "pb"

        @JvmStatic
        fun main(args: Array<String>) {
            var i = 0
            var mode: Mode = Mode.PLAIN
            if (args[i].startsWith("-")) {
                mode = Mode.read(args[i++])
            }
            val conf: String = args[i]
            PbModule(conf, mode).run()
        }
    }
}
