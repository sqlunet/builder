package org.sqlbuilder2.legacy

import org.sqlbuilder.common.Module
import java.io.IOException

class LegacyModule private constructor(
    private val args: Array<String>
) : Module(MODULE_ID, args[0], null) {

    override fun run() {
        checkNotNull(props)
        for (i in 2..<args.size) {
            if (args[i].startsWith("from=")) {
                props.setProperty("from", args[i].substring(5))
            }
            if (args[i].startsWith("to=")) {
                props.setProperty("to", args[i].substring(3))
            }
        }

        try {
            when (args[1]) {
                "synsets"   -> SynsetToSynsetProcessor(props).run()
                "sensekeys" -> SenseToSensekeyProcessor(props).run()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {

        const val MODULE_ID: String = "legacy"

        fun main(args: Array<String>) {
            LegacyModule(args).run()
        }
    }
}
