package org.sqlbuilder.su

import org.sigma.core.Logging
import org.sqlbuilder.common.Module
import org.sqlbuilder.common.Module.Mode.Companion.read
import org.sqlbuilder.common.NotFoundException
import java.io.IOException
import java.util.logging.LogManager

class SuModule(
    conf: String,
    mode: Mode
) : Module(MODULE_ID, conf, mode) {

    override fun run() {
        checkNotNull(props)

        try {
            when (mode) {
                Mode.PLAIN   -> SuProcessor(props).run()
                Mode.RESOLVE -> SuResolvingProcessor(props).run()
                Mode.UPDATE  -> SuUpdatingProcessor(props).run()
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

        const val MODULE_ID: String = "sumo"

        private fun setLogging() {
            try {
                Logging::class.java.getClassLoader().getResourceAsStream("logging.properties").use {
                     LogManager.getLogManager().readConfiguration(it)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun turnOffLogging() {
            setLogging()

            val classKey = "java.util.logging.config.class"
            val classValue = System.getProperty(classKey)
            if (classValue != null && !classValue.isEmpty()) {
                System.err.println("$classKey = $classValue")
            }
        }

        @Throws(NotFoundException::class)
        fun main(args: Array<String>) {
            turnOffLogging()

            var i = 0
            var mode = Mode.PLAIN
            if (args[i].startsWith("-")) {
                mode = read(args[i++])
            }
            val conf = args[i]
            SuModule(conf, mode).run()
        }
    }
}
