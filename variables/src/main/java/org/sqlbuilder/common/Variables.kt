/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.sqlbuilder.common

import java.io.*
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.Throws

/**
 * Variable substitution
 */
class Variables

/**
 * Constructor
 */
private constructor() {

    /**
     * Name values pairs
     */
    val toValue: MutableMap<String, String> = HashMap<String, String>()

    /**
     * Add key-value pair
     *
     * @param key   key
     * @param value value
     * @return old value if present, null otherwise
     */
    fun put(key: String, value: String): String? {
        return toValue.put(key, value)
    }

    /**
     * Substitute values to variables in file
     *
     * @param file     input file
     * @param ps       print stream
     * @param compress whether to compress spaces to single space
     * @param check    for remaining variable patterns
     * @throws IOException io exception
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun varSubstitutionInFile(file: File, ps: PrintStream, useBackticks: Boolean, compress: Boolean, check: Boolean = false) {
        try {
            FileInputStream(file).use {
                varSubstitutionInIS(it, ps, useBackticks, compress, check)
            }
        } catch (iae: IllegalArgumentException) {
            System.err.println("At $file\n${iae.message}")
            throw iae
        }
    }

    /**
     * Substitute values to variables in input stream
     *
     * @param input    input stream
     * @param ps       print stream
     * @param compress whether to compress spaces to single space
     * @param check    for remaining variable patterns
     * @throws IOException io exception
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun varSubstitutionInIS(input: InputStream, ps: PrintStream, useBackticks: Boolean, compress: Boolean, check: Boolean = false) {
        BufferedReader(InputStreamReader(input, Charset.defaultCharset())).use { reader ->
            var lineNum = 0
            var line: String
            while ((reader.readLine().also { line = it }) != null) {
                lineNum++
                try {
                    line = varSubstitution(line, useBackticks)
                } catch (iae: IllegalArgumentException) {
                    System.err.println("At line $line content: [$line]")
                    throw iae
                }
                if (compress) {
                    line = line.replace("\\s+".toRegex(), " ")
                }
                ps.println(line)

                // check
                if (check) {
                    check(line)
                }
            }
        }
    }

    /**
     * Substitute values to variables in string
     *
     * @param input input string
     * @return string with values substituted fir variable name
     */
    fun varSubstitution(input: String): String {
        return varSubstitution(varSubstitution(input, AT_PATTERN, false), DOLLAR_PATTERN, false)
    }

    /**
     * Check for remaining variable pattern in string
     *
     * @param input input string
     * @throws IllegalStateException illegal state exception
     */
    fun check(input: String) {
        val m: Matcher = VAR_PATTERN.matcher(input)
        require(!m.find()) { m.group(1) }
    }

    /**
     * Substitute values to variables in string
     *
     * @param input        input string
     * @param useBackticks whether to surround substitution result with back ticks
     * @return string with values substituted fir variable name
     */
    fun varSubstitution(input: String, useBackticks: Boolean): String {
        return varSubstitution(varSubstitution(input, AT_PATTERN, false), DOLLAR_PATTERN, useBackticks)
    }

    /**
     * Substitute values to variables in string
     *
     * @param input        input string
     * @param p            pattern for variable
     * @param useBackticks whether to surround substitution result with back ticks
     * @return string with values substituted for variable name
     */
    fun varSubstitution(input: String, p: Pattern, useBackticks: Boolean): String {
        val m = p.matcher(input)
        if (m.find()) {
            return m.replaceAll {
                val varName = it.group(1)
                require(toValue.containsKey(varName)) { varName!! }
                val v = toValue[varName]
                if (useBackticks) "`$v`" else v
            }
        }
        return input
    }

    /**
     * Substitute values to variables in string
     *
     * @param input input string
     * @param ps    patterns for variable
     * @return string with values substituted for variable name
     */
    fun varSubstitutions(input: String, vararg ps: Pattern): String {
        var str = input
        for (p in if (ps.isEmpty()) arrayOf(DOLLAR_PATTERN, AT_PATTERN, NUMBER_PATTERN) else ps) {
            str = varSubstitution(str, p, false)
        }
        return str
    }

    /**
     * Export key-value pairs to print stream
     */
    fun dumpVarsVals(ps: PrintStream) {
        toValue.entries
            .forEach {
                ps.println(it)
            }
    }

    /**
     * Export key-value pairs to print stream
     */
    fun export(ps: PrintStream) {
        toValue.keys.asSequence()
            .map { it to if (it.contains(".")) it.substring(it.lastIndexOf('.') + 1) else it }  // (key, key2)
            .filter { setOf("table", "file", "columns", "resolved").contains(it.second) }
            .sortedWith(Comparator.comparing<Pair<String, String>, String> { it.second })
            .map { it.second.uppercase() to toValue[it.first] }  // (key2, value)
            .distinct()
            .map { "public static final String ${it.first}=\"${it.second}\";" }
            .forEach { ps.println(it) }
    }

    companion object {

        private const val CURLY_WRAPPED = "\\{([a-zA-Z0-9_.]+)}"
        val NUMBER_PATTERN: Pattern = Pattern.compile("#$CURLY_WRAPPED")
        val DOLLAR_PATTERN: Pattern = Pattern.compile("\\$$CURLY_WRAPPED")
        val AT_PATTERN: Pattern = Pattern.compile("@$CURLY_WRAPPED")
        val VAR_PATTERN: Pattern = Pattern.compile("[@$]$CURLY_WRAPPED")

        /**
         * Set values in map from properties
         *
         * @param propertiesPaths resource bundle
         * @return variables
         */
        @Throws(IOException::class)
        fun make(vararg propertiesPaths: String): Variables {
            val vars = Variables()
            val properties = Properties()
            for (propertiesPath in propertiesPaths) {
                FileInputStream(propertiesPath).use {
                    properties.load(it)
                }
            }
            for (o in properties.keys) {
                val k: String = o.toString()
                vars.toValue.put(k, properties.getProperty(k))
            }
            return vars
        }

        /**
         * Set values in map from resource bundles
         *
         * @param bundles resource bundles
         * @return variables
         */
        fun make(vararg bundles: ResourceBundle): Variables {
            val vars = Variables()
            for (bundle in bundles) {
                for (k in bundle.keySet()) {
                    vars.toValue.put(k, bundle.getString(k))
                }
            }
            return vars
        }

        /**
         * Scan input and produces list on stderr with same value
         *
         * @param input input
         */
        @Throws(IOException::class)
        fun dumpVars(input: File, consumer: (String) -> Unit) {
            val p = Pattern.compile("[$@]\\{([a-zA-Z0-9_.]+)}")
            input.useLines {
                it
                    .flatMap { line: String ->
                        val vars = ArrayList<String>()
                        val m = p.matcher(line)
                        while (m.find()) {
                            val varName = m.group(1)
                            vars.add(varName)
                        }
                        vars.asSequence()
                    }
                    .sorted()
                    .distinct()
                    .forEach(consumer)
            }
        }

        /**
         * Scan input and produces list on stderr with same value
         *
         * @param input input
         */
        fun dumpVars(input: String) {
            val p = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)}")
            val m = p.matcher(input)
            if (m.find()) {
                val varName = m.group(1)
                System.err.println(varName)
            }
        }
    }
}
