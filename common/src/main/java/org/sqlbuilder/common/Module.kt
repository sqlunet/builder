package org.sqlbuilder.common

import java.io.File
import java.io.FileInputStream
import java.util.*

abstract class Module protected constructor(val id: String?, conf: String, @JvmField protected val mode: Mode?) {

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

    @JvmField
    protected val props: Properties = getProperties(conf)

    protected abstract fun run()

    companion object {

        @Nullable
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
