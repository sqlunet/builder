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
            println(C.invoke("one"))
            println(C.invoke("two"))
            println(C.invoke("three"))
            println(C.invoke("four"))
        }
        System.err.println("[AFTER]" + C.status())
        try {
            println(C.invoke("one"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println(C.invoke("two"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println(C.invoke("three"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println(C.invoke("four"))
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
            println(C.invoke("one"))
            println(C.invoke("two"))
            println(C.invoke("three"))
            println(C.invoke("four"))
        }
        System.err.println("[AFTER]" + C.status())
        println(C.invoke("one"))
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
                println("c " + C.invoke("one"))
                println("c " + C.invoke("two"))
                println("c " + C.invoke("three"))
                println("c " + C.invoke("four"))
                println("d " + D.invoke("one"))
                println("d " + D.invoke("two"))
                println("d " + D.invoke("three"))
                println("d " + D.invoke("four"))
            }
        }
        System.err.println("[AFTER]" + C.status())
        try {
            println("c " + C.invoke("one"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("c " + C.invoke("two"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("c " + C.invoke("three"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("c " + C.invoke("four"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + D.invoke("one"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + D.invoke("two"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + D.invoke("three"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + D.invoke("four"))
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
                println("c " + C.invoke("one"))
                println("c " + C.invoke("two"))
                println("c " + C.invoke("three"))
                println("c " + C.invoke("four"))
                println("d " + D.invoke("one"))
                println("d " + D.invoke("two"))
                println("d " + D.invoke("three"))
                println("d " + D.invoke("four"))
            }
        }
        System.err.println("[AFTER]" + C.status())
        println("c " + C.invoke("one"))
    }

    companion object {

        private val SET = mutableSetOf<String>("one", "two", "three", "four")

        private val COMPARATOR: Comparator<String> = Comparator.naturalOrder()

        private val C = SetCollector<String>(COMPARATOR)

        private val D = SetCollector<String>(COMPARATOR)
    }
}
