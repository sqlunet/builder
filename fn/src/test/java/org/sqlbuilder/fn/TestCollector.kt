package org.sqlbuilder.fn

import org.junit.Test
import org.sqlbuilder.common.SetCollector

class TestCollector {

    @Test
    fun test() {
        for (e in SET) {
            C.add(e)
        }

        System.err.println("[BEFORE]" + C.status())
        C.open().use {
            System.err.println("[ACTIVE]" + C.status())
            println(C.apply("one"))
            println(C.apply("two"))
            println(C.apply("three"))
            println(C.apply("four"))
        }
        System.err.println("[AFTER]" + C.status())
        try {
            println(C.apply("one"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println(C.apply("two"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println(C.apply("three"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println(C.apply("four"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testFail() {
        for (e in SET) {
            C.add(e)
        }

        System.err.println("[BEFORE]" + C.status())
        C.open().use {
            System.err.println("[ACTIVE]" + C.status())
            println(C.apply("one"))
            println(C.apply("two"))
            println(C.apply("three"))
            println(C.apply("four"))
        }
        System.err.println("[AFTER]" + C.status())
        println(C.apply("one"))
    }

    @Test
    fun test2() {
        for (e in SET) {
            C.add(e)
            D.add(e)
        }

        System.err.println("[BEFORE]" + C.status())
        C.open().use {
            D.open().use {
                System.err.println("[ACTIVE]" + C.status() + D.status())
                println("c " + C.apply("one"))
                println("c " + C.apply("two"))
                println("c " + C.apply("three"))
                println("c " + C.apply("four"))
                println("d " + D.apply("one"))
                println("d " + D.apply("two"))
                println("d " + D.apply("three"))
                println("d " + D.apply("four"))
            }
        }
        System.err.println("[AFTER]" + C.status())
        try {
            println("c " + C.apply("one"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("c " + C.apply("two"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("c " + C.apply("three"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("c " + C.apply("four"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + D.apply("one"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + D.apply("two"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + D.apply("three"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + D.apply("four"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun test2Fail() {
        for (e in SET) {
            C.add(e)
            D.add(e)
        }

        System.err.println("[BEFORE]" + C.status())
        C.open().use {
            D.open().use {
                System.err.println("[ACTIVE]" + C.status() + D.status())
                println("c " + C.apply("one"))
                println("c " + C.apply("two"))
                println("c " + C.apply("three"))
                println("c " + C.apply("four"))
                println("d " + D.apply("one"))
                println("d " + D.apply("two"))
                println("d " + D.apply("three"))
                println("d " + D.apply("four"))
            }
        }
        System.err.println("[AFTER]" + C.status())
        println("c " + C.apply("one"))
    }

    companion object {

        private val SET = mutableSetOf<String>("one", "two", "three", "four")

        private val COMPARATOR: Comparator<String> = Comparator.naturalOrder()

        private val C = SetCollector<String>(COMPARATOR)

        private val D = SetCollector<String>(COMPARATOR)
    }
}
