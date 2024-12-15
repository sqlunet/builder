package org.semantikos.common

import java.io.IOException

abstract class Processor(protected val tag: String) {

    @Throws(IOException::class)
    abstract fun run()
}
