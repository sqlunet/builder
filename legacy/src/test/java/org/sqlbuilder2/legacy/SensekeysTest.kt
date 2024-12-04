package org.sqlbuilder2.legacy

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.sqlbuilder2.legacy.SenseToSensekeyProcessor.Companion.getLemmaPosOffsetToSensekey
import java.io.File
import java.io.IOException
import kotlin.Throws

class SensekeysTest {

    @Test
    fun testTripletsMapInt() {
        val t = Triple("baby", 'n', 796767)
        val sk: String? = map[t]
        Assertions.assertNotEquals(null, sk)
        println(sk)
    }

    @Test
    fun testTripletsMapLong() {
        val t = Triple("baby", 'n', 796767L)
        val sk: String? = map[t]
        Assertions.assertEquals(null, sk)
    }

    @Test
    fun testTripletsLongEq() {
        val t1 = Triple("baby", 'n', 796767L)
        val t2 = Triple("baby", 'n', 796767L)
        Assertions.assertEquals(t1, t2)
    }

    @Test
    fun testTripletsIntEq() {
        val t1 = Triple("baby", 'n', 796767)
        val t2 = Triple("baby", 'n', 796767)
        Assertions.assertEquals(t1, t2)
    }

    @Test
    fun testTripletsLongIntEq() {
        val t1 = Triple("baby", 'n', 796767)
        val t2 = Triple("baby", 'n', 7967678L)
        Assertions.assertThrows<AssertionError?>(AssertionError::class.java, Executable {
            Assertions.assertEquals(t1, t2)
        })
    }

    @Test
    fun testTripletsLong() {
        val t1 = Triple("baby", 'n', 796767L)
        val t2 = Triple("baby", 'n', 796768L)
        Assertions.assertThrows<AssertionError?>(AssertionError::class.java, Executable {
            Assertions.assertEquals(t1, t2)
        })
    }

    @Test
    fun testTripletsInt() {
        val t1 = Triple("baby", 'n', 796767)
        val t2 = Triple("baby", 'n', 796768)
        Assertions.assertThrows<AssertionError?>(AssertionError::class.java, Executable {
            Assertions.assertEquals(t1, t2)
        })
    }

    @Test
    fun testTripletsLongInt() {
        val t1 = Triple("baby", 'n', 796767)
        val t2 = Triple("baby", 'n', 796768L)
        Assertions.assertThrows<AssertionError?>(AssertionError::class.java, Executable {
            Assertions.assertEquals(t1, t2)
        })
    }

    companion object {

        private lateinit var map: Map<Triple<String, Char, Number>, String>

        @JvmStatic
        @BeforeAll
        @Throws(IOException::class)
        fun init() {
            @Suppress("UNCHECKED_CAST")
            map = getLemmaPosOffsetToSensekey(File("data/YY/index.sense")) as Map<Triple<String, Char, Number>, String>
            println(map)
        }
    }
}
