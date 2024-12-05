/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.sqlbuilder.common

import org.sqlbuilder.common.Variables.Companion.make
import java.io.*
import java.net.URISyntaxException
import java.nio.file.Paths
import java.util.*
import java.util.jar.JarFile
import kotlin.system.exitProcess

/**
 * Main class that generates the SQL schema by instantiating templates
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
class SchemaGenerator(private val variables: Variables) {

    /**
     * Generate schema
     *
     * @param module      module
     * @param output      output
     * @param inputSubdir input subdir (from sqltemplates/)
     * @param inputs      input files (or null if all)
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun generate(module: String, output: String, inputSubdir: String, inputs: Array<String>) {

        var outputFileOrDir: File? = null

        // Output
        if ("-" != output) {
            // output is not console
            if (output.endsWith(".sql")) {
                // output is plain sql file
                System.err.println("Output to file $output")
                outputFileOrDir = File(output)
                if (outputFileOrDir.exists()) {
                    System.err.println("Overwrite ${outputFileOrDir.absolutePath}")
                    exitProcess(1)
                }
                outputFileOrDir.createNewFile()
            } else {
                // multiple outputs as per inputs
                outputFileOrDir = File(output)
                if (!outputFileOrDir.exists()) {
                    outputFileOrDir.mkdirs()
                }
            }
        }

        // Input
        // Single output if console or file
        if (outputFileOrDir == null || outputFileOrDir.isFile()) {
            if (outputFileOrDir == null) System.out else PrintStream(outputFileOrDir).use {
                processTemplates(module, inputSubdir, inputs) { input: InputStream, name: String ->
                    try {
                        variables.varSubstitutionInIS(input, it, true, true)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (outputFileOrDir.isDirectory()) {
            val dir = outputFileOrDir
            processTemplates(module, inputSubdir, inputs) { input: InputStream, name: String ->
                System.err.println(name)
                val output = File(dir, name)
                try {
                    PrintStream(output).use {
                        variables.varSubstitutionInIS(input, it, true, true)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            System.err.println("Internal error")
        }
    }

    /**
     * Process input template files
     *
     * @param module   module
     * @param path     path of inputs
     * @param inputs   inputs
     * @param consumer string consumer
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun processTemplates(module: String, path: String, inputs: Array<String>?, consumer: (InputStream, String) -> Unit) {
        // external resources
        if (inputs != null && inputs.isNotEmpty()) {
            for (input in inputs) {
                val file = File(path, input)
                val fileName = Paths.get(input).fileName.toString()
                FileInputStream(file).use { fis ->
                    consumer.invoke(fis, fileName)
                }
            }
            return
        }

        // internal resources
        val jarFile = File(javaClass.getProtectionDomain().codeSource.location.path)
        if (jarFile.isFile()) {
            // Run with JAR file
            val prefix = "$module/sqltemplates/$path/"
            JarFile(jarFile).use { jar ->
                val entries = jar.entries() //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    if (entry.isDirectory) {
                        continue
                    }

                    val name = entry.getName()
                    //filter according to the path
                    if (name.startsWith(prefix)) {
                        val fileName = Paths.get(name).fileName.toString()
                        jar.getInputStream(entry).use {
                            consumer.invoke(it, fileName)
                        }
                    }
                }
            }
        } else {
            // Run with IDE
            val url = SchemaGenerator::class.java.getResource("/$module/sqltemplates/$path")
            if (url != null) {
                try {
                    val dir = File(url.toURI())
                    val files = dir.listFiles()
                    if (files != null) {
                        for (file in files) {
                            FileInputStream(file).use {
                                consumer.invoke(it, file.getName())
                            }
                        }
                    } else {
                        throw RuntimeException("Dir:$dir is empty")
                    }
                } catch (_: URISyntaxException) {
                    // never happens
                }
            }
        }
    }

    companion object {

        /**
         * Main entry point
         *
         * @param args command-line arguments
         * @throws IOException io exception
         */
        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            var args = args
            var compat = false
            if ("-compat" == args[0]) {
                compat = true
                args = args.copyOfRange(1, args.size)
            }
            val module = args[0]
            val output = args[1]
            val inputSubdir = args[2]
            val inputs = args.copyOfRange<String>(3, args.size)
            val bundle = ResourceBundle.getBundle("$module/${if (compat) "NamesCompat" else "Names"}")
            val variables = make(bundle)
            SchemaGenerator(variables).generate(module, output, inputSubdir, inputs)
        }
    }
}
