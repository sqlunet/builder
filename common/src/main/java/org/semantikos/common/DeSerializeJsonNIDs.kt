/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.semantikos.common

import kotlinx.serialization.json.Json
import java.io.File
import kotlin.system.exitProcess

/**
 * Deserialize ID to Numeric IDs maps
 */
object DeSerializeJsonNIDs {

    const val NID_PREFIX: String = ""

    const val WORDS_FILE: String = "words"

    const val SENSES_FILE: String = "senses"
    const val SENSES_WORDS_FILE: String = "senseswords"

    const val SYNSETS_FILE: String = "synsets"

    const val SENSES_WORDS_SYNSETS_FILE: String = "sensekeys_words_synsets"

    const val EXTENSION: String = ".json"

    /**
     * Deserialize map in file
     * @param file file
     * @return id-to-nid map
     */
    fun deserializeJsonIntMap(file: File): Map<String, Int> {
        //val element = Json.parseToJsonElement(file.readText())
        //return element.jsonObject.mapValues { it.value.jsonPrimitive.int }
        return Json.decodeFromString(file.readText())
    }

    /**
     * Deserialize map in file
     * @param file file
     * @return id-to-nid map
     */
    fun deserializeJsonIntsMap(file: File): Map<String, List<Int>> {
        //val element = Json.parseToJsonElement(file.readText())
        //return element.jsonObject.mapValues { it.value.jsonArray.map { it.jsonPrimitive.int }}
        return Json.decodeFromString(file.readText())
    }

    /**
     * Deserialize map in file
     * @param file file
     * @return id-to-nid map
     */
    inline fun <reified V> deserializeJsonMap(file: File): Map<String, V> {
        return Json.decodeFromString(file.readText())
    }

    /**
     * Deserialize id-to_nid maps
     *
     * @param inDir input directory
     * @return id-to-nid maps indexed by name
     * @throws ClassNotFoundException class not found exception
     */
    fun deserializeAllNIDs(inDir: File): Map<String, Map<String, *>> {
        return mapOf(
            WORDS_FILE to deserializeJsonMap<Int>(File(inDir, "$NID_PREFIX$WORDS_FILE$EXTENSION")),
            SENSES_FILE to deserializeJsonMap<Int>(File(inDir, "$NID_PREFIX$SENSES_FILE$EXTENSION")),
            SENSES_WORDS_FILE to deserializeJsonMap<Int>(File(inDir, "$NID_PREFIX$SENSES_WORDS_FILE$EXTENSION")),
            SYNSETS_FILE to deserializeJsonMap<Int>(File(inDir, "$NID_PREFIX$SYNSETS_FILE$EXTENSION")),
            SENSES_WORDS_SYNSETS_FILE to deserializeJsonMap<List<Int>>(File(inDir, "$NID_PREFIX$SENSES_WORDS_SYNSETS_FILE$EXTENSION")),
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
        maps.keys.forEach { mk ->
            val m = maps[mk]!!
            println("$mk ${m.size}")
            m.asSequence().take(10).forEach { (k, v) -> println("\t$k -> $v") }
            //println(maps[k]!!.entries.iterator().next().javaClass)
        }
    }
}
