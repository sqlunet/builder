package org.sqlbuilder2.ser

import java.io.*

object Serialize {

    /**
     * Serialize object to file
     *
     * @param obj object
     * @param outFile output file
     * @throws IOException io exception
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
