package org.sqlbuilder.fn

import org.junit.BeforeClass
import org.junit.Test
import org.sqlbuilder.fn.collectors.FnEnumCollector
import org.sqlbuilder.fn.collectors.FnEnumCollector.Companion.coreTypes
import org.sqlbuilder.fn.collectors.FnEnumCollector.Companion.labelITypes
import org.sqlbuilder.fn.objects.Values
import org.sqlbuilder.fn.objects.Values.LabelIType

class Presets {

    @Test
    fun presetsPoses() {
        println("\nPOSes:")
        for (s in coreTypes) {
            println(s)
        }
    }

    @Test
    fun presetsPosDataRows() {
        Values.Pos.MAP.forEach { (key: Values.Pos, value: Int) -> println("$value,${key.dataRow()}") }
    }

    @Test
    fun presetsCoreTypes() {
        println("\nCORETYPEs:")
        for (s in coreTypes) {
            println(s)
        }
    }

    @Test
    fun presetsCoreTypeDataRows() {
        Values.CoreType.MAP.forEach { (key: Values.CoreType, value: Int) -> println("$value,${key.dataRow()}") }
    }

    @Test
    fun presetsITypes() {
        println("\nLABELITYPEs:")
        for (s in labelITypes) {
            println(s)
        }
    }

    @Test
    fun presetsITypeDataRows() {
        LabelIType.MAP.forEach { (key: LabelIType, value: Int) -> println("$value,${key.dataRow()}") }
    }

    companion object {

        @BeforeClass
        fun init() {
            FnEnumCollector().run()
        }
    }
}
