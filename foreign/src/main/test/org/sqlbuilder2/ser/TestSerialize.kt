package org.sqlbuilder2.ser

import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.sqlbuilder2.ser.DeSerialize.deserialize
import org.sqlbuilder2.ser.Serialize.serialize
import java.io.File

class TestSerialize {

    @Test
    fun testKotlinPair() {
        val o = 1 to 2
        serialize(o, File("test.ser"))

        val o2: Any = deserialize(File("test.ser"))
        println(o2)
        Assert.assertEquals(o, o2)
    }

    companion object {

        @BeforeClass
        @JvmStatic
        fun init() {
        }
    }
}
