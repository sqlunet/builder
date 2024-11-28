package org.sqlbuilder.common

import java.io.IOException

abstract class Processor(@JvmField protected val tag: String) {

    @Throws(IOException::class)
    abstract fun run()
}
