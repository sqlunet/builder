/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.semantikos.common

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import kotlin.system.exitProcess

/**
 * Deserialize ID to Numeric IDs maps
 */
object DeSerializeJsonNIDs {

    const val NID_PREFIX: String = ""

    const val WORDS_FILE: String = "words"

    const val SENSES_FILE: String = "senses"
    const val SENSES_WORDS_FILE: String = "senses_words"

    const val SYNSETS_FILE: String = "synsets"

    const val EXTENSION: String = ".json"

    /**
     * Deserialize map in file
     * @param file file
     * @return id-to-nid map
     */
    fun deserializeJsonMap(file: File): Map<String, Int> {
        val element = Json.parseToJsonElement(file.readText())
        return element.jsonObject.mapValues { it.value.jsonPrimitive.int }
    }

    /**
     * Deserialize id-to_nid maps
     *
     * @param inDir input directory
     * @return id-to-nid maps indexed by name
     * @throws ClassNotFoundException class not found exception
     */
    fun deserializeAllNIDs(inDir: File): Map<String, Map<String, Int>> {
        return mapOf(
            WORDS_FILE to deserializeJsonMap(File(inDir, "$NID_PREFIX$WORDS_FILE$EXTENSION")),
            SENSES_FILE to deserializeJsonMap(File(inDir, "$NID_PREFIX$SENSES_FILE$EXTENSION")),
            SENSES_WORDS_FILE to deserializeJsonMap(File(inDir, "$NID_PREFIX${SENSES_WORDS_FILE}_FILE$EXTENSION")),
            SYNSETS_FILE to deserializeJsonMap(File(inDir, "$NID_PREFIX$SYNSETS_FILE$EXTENSION")),
        )
    }

    /**
     * Main
     *
     * @param args command-line arguments
     * @throws ClassNotFoundException class not found exception
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val inDir = File(args[0])
        if (!inDir.isDirectory) {
            exitProcess(1)
        }
        val maps = deserializeAllNIDs(inDir)
        println("$WORDS_FILE ${maps[WORDS_FILE]!!.size}")
        //println(maps[WORDS_FILE]!!.entries.iterator().next().javaClass)
        println("$SENSES_FILE ${maps[SENSES_FILE]!!.size}")
        //println(maps[SENSES_FILE]!!.entries.iterator().next().javaClass)
        //println(maps[SENSES_WORDS_FILE]!!.entries.iterator().next().javaClass)
        println("$SYNSETS_FILE ${maps[SYNSETS_FILE]!!.size}")
        //println(maps[SYNSETS_FILE]!!.entries.iterator().next().javaClass)
    }
}
