/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.sqlbuilder

import org.sqlbuilder.bnc.BncModule
import org.sqlbuilder.fn.FnModule
import org.sqlbuilder.pb.PbModule
import org.sqlbuilder.pm.PmModule
import org.sqlbuilder.sl.SlModule
import org.sqlbuilder.sn.SnModule
import org.sqlbuilder.su.SuModule
import org.sqlbuilder.vn.VnModule
import org.sqlbuilder2.legacy.LegacyModule
import java.io.File

/**
 * Main class
 *
 * @author Bernard Bou
 */
class Main {

    companion object {

        val modules = listOf(
            "bnc",
            "sn",
            "vn",
            "pb",
            "sl",
            "fn",
            "pm",
            "su",
            "legacy",
        )

        fun run0(module: String, args: Array<String>) {
            when (module) {
                "bnc" -> BncModule.main(args)
                "sn"  -> SnModule.main(args)
                "vn"  -> VnModule.main(args)
                "pb"  -> PbModule.main(args)
                "sl"  -> SlModule.main(args)
                "fn"  -> FnModule.main(args)
                "pm"  -> PmModule.main(args)
                "su"  -> SuModule.main(args)
                "legacy" -> LegacyModule.main(args)
                else  -> {}
            }
        }

        fun run(module: String, args: Array<String>) {
            println("$module ${args.joinToString(separator = " ")}")
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val cwd = File(".").absolutePath
            println("CWD: $cwd")
            val module = args[0]
            val ops = args.slice(1..args.size - 1)
            val runModules = if ("all" == module) modules else listOf(module)
            runModules.forEach { m ->
                println("\nMOD: $m")
                ops.forEach { o ->
                    val args = if ("all" == o) arrayOf("-data", "-resolve", "-update", "-export") else arrayOf(o)
                    args.forEach {
                        run(m, if ("-data" == it) arrayOf("$m.properties") else arrayOf(it, "$m.properties"))
                    }
                }
            }
        }
    }
}
