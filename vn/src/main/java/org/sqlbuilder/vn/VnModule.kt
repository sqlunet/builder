package org.sqlbuilder.vn

import org.sqlbuilder.common.Module
import org.sqlbuilder.common.Module.Mode.Companion.read
import org.sqlbuilder.vn.collector.VnCollector
import org.sqlbuilder.vn.collector.VnExportCollector
import org.sqlbuilder.vn.collector.VnUpdateCollector
import java.io.IOException

class VnModule(conf: String, mode: Mode) : Module(MODULE_ID, conf, mode) {

    override fun run() {
        checkNotNull(props)

        when (mode) {
            Mode.PLAIN, Mode.RESOLVE -> {
                VnCollector(props).run()
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
                VnUpdateCollector(props).run()
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
                VnExportCollector(props).run()
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

        const val MODULE_ID: String = "vn"

        fun main(args: Array<String>) {
            var i = 0
            var mode = Mode.PLAIN
            if (args[i].startsWith("-")) {
                mode = read(args[i++])
            }
            val conf = args[i]
            VnModule(conf, mode).run()
        }
    }
}
