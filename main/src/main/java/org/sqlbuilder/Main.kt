/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.sqlbuilder

import org.sqlbuilder.bnc.BncModule
import org.sqlbuilder.common.AnsiColors.grey
import org.sqlbuilder.common.AnsiColors.magenta
import org.sqlbuilder.fn.FnModule
import org.sqlbuilder.pb.PbModule
import org.sqlbuilder.pm.PmModule
import org.sqlbuilder.sl.SlModule
import org.sqlbuilder.sn.SnModule
import org.sqlbuilder.su.SuModule
import org.sqlbuilder.vn.VnModule
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
        )

        private fun getProperties(conf: String, clazz: Class<*>): Properties {
            val props = Properties()
            val url: URL = clazz.getResource(conf)!!
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

        fun forkJvm(mainClass: String, args: List<String> = emptyList(), cwd: File = File(".")): Process {
            val javaHome = System.getProperty("java.home")
            val javaExec = File(javaHome, "bin/java").absolutePath
            val classpath = System.getProperty("java.class.path")
            val command = listOf(javaExec, "-cp", classpath, mainClass) + args
            val process = ProcessBuilder(command)
                .directory(cwd)
                .inheritIO() // Inherit I/O to see the output in the current console
                .start()
            println(grey(">Fork PID ${process.pid()}"))
            return process
        }

        fun mainClass(module: String): Class<*>? {
            return when (module) {
                "bnc" -> BncModule::class.java
                "sn"  -> SnModule::class.java
                "vn"  -> VnModule::class.java
                "pb"  -> PbModule::class.java
                "sl"  -> SlModule::class.java
                "fn"  -> FnModule::class.java
                "pm"  -> PmModule::class.java
                "su"  -> SuModule::class.java
                else  -> null
            }
        }

        fun run(module: String, args: List<String>) {
            val mainClass = mainClass(module)
            if (mainClass != null) {
                val process = forkJvm(mainClass.canonicalName, args, File(".", module))
                val exitCode = process.waitFor()
                println(grey("<Fork PID ${process.pid()} with exit code $exitCode"))
            }
        }

        fun data(module: String, args: List<String>): File {
            val confPath = args[args.size - 1]
            val top = File(module)
            val conf = File(top, confPath)
            val props = getProperties(conf.absolutePath)
            val dataPath = props["${module}_home"] as String
            val data = File(top, dataPath)
            return data
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val cwd = File(".").absolutePath
            println("CWD: $cwd")
            val module = args[0]
            val ops = args.slice(1..args.size - 1)
            val runModules = if ("all" == module) modules else listOf(module)
            runModules.forEach { m ->
                ops.forEach { o ->
                    val args = if ("all" == o) arrayOf("data", "resolve", "update", "export") else arrayOf(o)
                    args.forEach {
                        val args = if ("data" == it) listOf("$m.properties") else listOf("-$it", "$m.properties")
                        val data = data(m, args)
                        println(magenta("MODULE:$m OP:$o DATA:$data->${data.absolutePath} ARGS:${args.joinToString(separator = " ")}"))
                        run(m, args)
                    }
                }
            }
        }
    }
}

