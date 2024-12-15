package org.semantikos.legacy

import org.semantikos.common.Module
import java.io.IOException

class LegacyModule private constructor(
    private val args: Array<String>,
) : Module(MODULE_ID, args[0], null) {

    override fun run() {
        checkNotNull(props)
        if (args.size < 3) {
            System.err.println("Incorrect arguments")
            return
        }
        for (i in 2..<args.size) {
            when {
                args[i].startsWith("from=") -> props.setProperty("from", args[i].substring(5))
                args[i].startsWith("to=")   -> props.setProperty("to", args[i].substring(3))
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

        @JvmStatic
        fun main(args: Array<String>) {
            LegacyModule(args).run()
        }
    }
}
