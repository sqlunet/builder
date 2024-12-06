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
import java.io.FileInputStream
import java.io.IOException
import java.net.URL
import java.util.*

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

        private fun getProperties0(conf: String): Properties {
            val props = Properties()
            val url: URL = Main::class.java.getResource(conf)!!
            try {
                url.openStream().use {
                    props.load(it)
                }
            } catch (e: IOException) {
                System.err.println(e.message)
            }
            return props
        }

        private fun getProperties(conf: String): Properties {
            val props = Properties()
            val f = File(conf)
            try {
                FileInputStream(f).use {
                    props.load(it)
                }
            } catch (e: IOException) {
                System.err.println(e.message)
            }
            return props
        }

        fun run0(module: String, args: Array<String>) {
            when (module) {
                "bnc"    -> BncModule.main(args)
                "sn"     -> SnModule.main(args)
                "vn"     -> VnModule.main(args)
                "pb"     -> PbModule.main(args)
                "sl"     -> SlModule.main(args)
                "fn"     -> FnModule.main(args)
                "pm"     -> PmModule.main(args)
                "su"     -> SuModule.main(args)
                "legacy" -> LegacyModule.main(args)
                else     -> {}
            }
        }

        fun run(module: String, args: Array<String>) {
            println("$module ${args.joinToString(separator = " ")}")
            val conf = args[args.size - 1]

            val props = getProperties("${module}/$conf")
            println(props["${module}_home"])
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
