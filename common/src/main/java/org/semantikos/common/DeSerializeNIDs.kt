/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.semantikos.common

import org.semantikos.common.DeSerializeJsonNIDs.SENSES_WORDS_SYNSETS_FILE
import java.io.*
import kotlin.system.exitProcess

/**
 * Deserialize ID to Numeric IDs maps
 */
object DeSerializeNIDs {

    const val NID_PREFIX: String = "nid_"

    const val WORDS_FILE: String = "words"

    const val SENSES_FILE: String = "senses"

    const val SYNSETS_FILE: String = "synsets"

    const val SER_EXTENSION: String = ".ser"

    /**
     * Safe cast
     *
     * @param obj object
     * @param <T> cast type
     * @return cast object
     */
    private fun <T> safeCast(obj: Any): T {
        @Suppress("UNCHECKED_CAST")
        return obj as T
    }

    /**
     * Deserialize id-to_nid maps
     *
     * @param inDir input directory
     * @return id-to-nid map indexed by name
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deserializeAllNIDs(inDir: File): Map<String, Map<String, *>> {
        val maps = HashMap<String, Map<String, *>>()
        FileInputStream(File(inDir, "$NID_PREFIX$WORDS_FILE$SER_EXTENSION")).use {
            val m = deSerializeNIDs(it)
            maps.put(WORDS_FILE, m)
        }
        FileInputStream(File(inDir, "$NID_PREFIX$SENSES_FILE$SER_EXTENSION")).use {
            val m = deSerializeNIDs(it)
            maps.put(SENSES_FILE, m)
        }
        FileInputStream(File(inDir, "$NID_PREFIX$SYNSETS_FILE$SER_EXTENSION")).use {
            val m = deSerializeNIDs(it)
            maps.put(SYNSETS_FILE, m)
        }
        FileInputStream(File(inDir, "$SENSES_WORDS_SYNSETS_FILE$SER_EXTENSION")).use {
            val m = deSerializeNIDTuples(it)
            maps.put(SENSES_WORDS_SYNSETS_FILE, m)
        }
        return maps
    }

    /**
     * Deserialize id-to_nid maps
     *
     * @param inFile input file
     * @return id-to-nid map indexed by name
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deserializeNIDs(inFile: File): Map<String, Int> {
        FileInputStream(inFile).use {
            return deSerializeNIDs(it)
        }
    }

    /**
     * Deserialize id-to_nid map
     *
     * @param input input stream
     * @return id-to-nid map
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deSerializeNIDs(input: InputStream): Map<String, Int> {
        return safeCast(deSerialize(input))
    }

    /**
     * Deserialize id-to_nid tuple map
     *
     * @param input input stream
     * @return id-to-nid tuple map
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deSerializeNIDTuples(input: InputStream): Map<String, List<Int>> {
        return safeCast(deSerialize(input))
    }

    /**
     * Deserialize object
     *
     * @param input input stream
     * @return object
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun deSerialize(input: InputStream): Any {
        ObjectInputStream(input).use {
            return it.readObject()
        }
    }

    /**
     * Main
     *
     * @param args command-line arguments
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @JvmStatic
    @Throws(IOException::class, ClassNotFoundException::class)
    fun main(args: Array<String>) {
        val inDir = File(args[0])
        if (!inDir.isDirectory) {
            exitProcess(1)
        }
        val maps = deserializeAllNIDs(inDir)
        maps.keys.forEach { mk ->
            val m = maps[mk]!!
            println("$mk ${m.size}")
            m.asSequence().take(10).forEach { (k, v) -> println("\t$k -> $v") }
            //println(maps[k]!!.entries.iterator().next().javaClass)
        }
    }
}
