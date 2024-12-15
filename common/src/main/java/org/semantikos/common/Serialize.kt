package org.semantikos.common

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.OutputStream

object Serialize {

    /**
     * Serialize object to file
     *
     * @param obj object
     * @param outFile output file
     * @throws java.io.IOException io exception
     */
    @Throws(IOException::class)
    fun serialize(obj: Any?, outFile: File) {
        FileOutputStream(outFile).use {
            serialize(it, obj)
        }
    }

    /**
     * Serialize object to output stream
     *
     * @param os output stream
     * @param obj object
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun serialize(os: OutputStream, obj: Any?) {
        ObjectOutputStream(os).use {
            it.writeObject(obj)
        }
    }
}