package org.sqlbuilder.common

import org.junit.Assert
import org.junit.Test
import org.sqlbuilder.common.Serialize.serialize
import java.io.File

class TestSerialize {

    @Test
    fun testKotlinPair() {
        val o = 1 to 2
        serialize(o, File("test.ser"))

        val o2: Any = DeSerialize.deserialize(File("test.ser"))
        println(o2)
        Assert.assertEquals(o, o2)
    }

    @Test
    fun testKotlinTriple() {
        val o = Triple(1,2,3)
        serialize(o, File("test.ser"))

        val o2: Any = DeSerialize.deserialize(File("test.ser"))
        println(o2)
        Assert.assertEquals(o, o2)
    }

    companion object {

        // @JvmStatic
        // @BeforeClass
        // fun init() {
        //     // empty
        // }
    }
}