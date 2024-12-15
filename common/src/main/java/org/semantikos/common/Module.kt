package org.semantikos.common

import java.io.File
import java.io.FileInputStream
import java.util.*

abstract class Module protected constructor(val id: String?, conf: String, protected val mode: Mode?) {

    enum class Mode {
        PLAIN, RESOLVE, UPDATE, EXPORT;

        companion object {

                    fun read(arg: String): Mode {
                return when (arg) {
                    "-resolve" -> RESOLVE
                    "-update"  -> UPDATE
                    "-export"  -> EXPORT
                    else       -> PLAIN
                }
            }
        }
    }

    protected val props: Properties = getProperties(conf)

    protected abstract fun run()

    companion object {

        fun getProperties(conf: String): Properties {
            val confFile = File(conf)
            FileInputStream(confFile).use {
                val props = Properties()
                props.load(it)
                return props
            }
        }
    }
}
