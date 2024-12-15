package org.semantikos.fn

import org.semantikos.common.Module
import org.semantikos.common.Module.Mode.Companion.read
import org.semantikos.fn.collectors.*
import java.io.IOException

class FnModule(
    conf: String, mode: Mode
) : Module(MODULE_ID, conf, mode) {

    override fun run() {
        checkNotNull(props)

        when (mode) {
            Mode.PLAIN, Mode.RESOLVE -> {
                FnEnumCollector().run()
                FnSemTypeCollector("semTypes.xml", props).run()
                FnFrameCollector(props).run()
                FnLexUnitCollector(props).run()
                FnFullTextCollector(props).run()
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
                FnWordCollector(props).run()
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
                FnFrameExportCollector(props).run()
                FnLexUnitExportCollector(props).run()
                FnWordCollector(props).run()
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

        const val MODULE_ID: String = "fn"

        @JvmStatic
        fun main(args: Array<String>) {
            var i = 0
            var mode = Mode.PLAIN
            if (args[i].startsWith("-")) {
                mode = read(args[i++])
            }
            val conf = args[i]
            FnModule(conf, mode).run()
        }
    }
}
