package org.semantikos.common

import org.semantikos.common.Utils.backtick
import java.io.IOException
import java.net.URL
import java.util.*

class Names(module: String) {

    private val props: Properties = getProperties("/$module/Names.properties")

    private fun getProperties(conf: String): Properties {
        val props = Properties()
        val url: URL = Names::class.java.getResource(conf)!!
        try {
            url.openStream().use {
                props.load(it)
            }
        } catch (e: IOException) {
            System.err.println(e.message)
        }
        return props
    }

    private fun get(key: String): String {
        val v = props.getProperty(key)
        requireNotNull(v) { "$key is not defined" }
        return v
    }

    private fun getNullable(key: String): String? {
        return props.getProperty(key)
    }

    fun file(key: String): String {
        return get("$key.file")
    }

    fun updateFile(key: String): String {
        return "update_" + get("$key.file")
    }

    fun updateFileNullable(key: String): String? {
        val f = getNullable("$key.file")
        return if (f == null) null else "update_$f"
    }

    fun serFile(key: String): String {
        return serFile(key, "")
    }

    fun serFile(name: String, suffix: String): String {
        return "$name$suffix.ser"
    }

    fun mapFile(key: String): String {
        return mapFile(key, "")
    }

    fun mapFile(name: String, suffix: String): String {
        return "$name$suffix.map"
    }

    fun header(key: String): String {
        return get("$key.header")
    }

    fun table(key: String): String {
        return backtick(get("$key.table"))
    }

    fun columns(key: String, resolve: Boolean = false): String {
        return backtickAll(get(key + (if (resolve) ".columns.resolved" else ".columns")))
    }

    fun column(key: String): String {
        return backtick(get(key))
    }

    private fun backtickAll(columns: String): String {
        return columns
            .split(",".toRegex())
            .dropLastWhile { it.isEmpty() }
            .joinToString(separator = ",") { backtick(it) }
    }
}