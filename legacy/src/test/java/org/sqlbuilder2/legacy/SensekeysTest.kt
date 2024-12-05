package org.sqlbuilder2.legacy

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.sqlbuilder2.legacy.SenseToSensekeyProcessor.Companion.getLemmaPosOffsetToSensekey
import java.io.File
import java.io.IOException

class SensekeysTest {

    @Test
    fun testResolution() {
        val t = LegacyLemmaPosOffsetResolvable("baby", 'n', 796767)
        val sk: String? = map[t]
        Assertions.assertNotEquals(null, sk)
        println(sk)
    }

    @Test
    fun testResolvableEq() {
        val t1 = LegacyLemmaPosOffsetResolvable("baby", 'n', 796767)
        val t2 = LegacyLemmaPosOffsetResolvable("baby", 'n', 796767)
        Assertions.assertEquals(t1, t2)
    }

    companion object {

        private lateinit var map: Map<LegacyLemmaPosOffsetResolvable, LegacyLemmaPosOffsetResolved>

        @BeforeAll
        @Throws(IOException::class)
        fun init() {
            @Suppress("UNCHECKED_CAST")
            map = getLemmaPosOffsetToSensekey(File("data/YY/index.sense"))
            println(map)
        }
    }
}
