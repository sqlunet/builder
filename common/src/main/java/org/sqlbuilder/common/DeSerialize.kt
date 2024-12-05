package org.sqlbuilder.common

import java.io.*

/**
 * Deserializer
 */
object DeSerialize {

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
     * Deserialize
     *
     * @param inFile serialization file
     * @param <T>    type of result
     * @return deserialized object
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
    </T> */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun <T> deserialize(inFile: File): T {
        return safeCast<T>(deSerialize(inFile))
    }

    /**
     * Deserialize object from file
     *
     * @param inFile input file
     * @return object
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deSerialize(inFile: File): Any {
        FileInputStream(inFile).use {
            return deSerialize(it)
        }
    }

    /**
     * Deserialize object from input stream
     *
     * @param input input stream
     * @return object
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deSerialize(input: InputStream): Any {
        ObjectInputStream(input).use {
            return it.readObject()
        }
    }
}
