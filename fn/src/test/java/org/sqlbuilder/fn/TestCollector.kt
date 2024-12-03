package org.sqlbuilder.fn

import org.junit.Test
import org.sqlbuilder.common.SetCollector

class TestCollector {

    @Test
    fun test() {
        for (e in TestCollector.Companion.SET) {
            TestCollector.Companion.C.add(e)
        }

        System.err.println("[BEFORE]" + TestCollector.Companion.C.status())
        TestCollector.Companion.C.open().use { ignored ->
            System.err.println("[ACTIVE]" + TestCollector.Companion.C.status())
            println(TestCollector.Companion.C.apply("one"))
            println(TestCollector.Companion.C.apply("two"))
            println(TestCollector.Companion.C.apply("three"))
            println(TestCollector.Companion.C.apply("four"))
        }
        System.err.println("[AFTER]" + TestCollector.Companion.C.status())
        try {
            println(TestCollector.Companion.C.apply("one"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println(TestCollector.Companion.C.apply("two"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println(TestCollector.Companion.C.apply("three"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println(TestCollector.Companion.C.apply("four"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testFail() {
        for (e in TestCollector.Companion.SET) {
            TestCollector.Companion.C.add(e)
        }

        System.err.println("[BEFORE]" + TestCollector.Companion.C.status())
        TestCollector.Companion.C.open().use { ignored ->
            System.err.println("[ACTIVE]" + TestCollector.Companion.C.status())
            println(TestCollector.Companion.C.apply("one"))
            println(TestCollector.Companion.C.apply("two"))
            println(TestCollector.Companion.C.apply("three"))
            println(TestCollector.Companion.C.apply("four"))
        }
        System.err.println("[AFTER]" + TestCollector.Companion.C.status())
        println(TestCollector.Companion.C.apply("one"))
    }

    @Test
    fun test2() {
        for (e in TestCollector.Companion.SET) {
            TestCollector.Companion.C.add(e)
            TestCollector.Companion.D.add(e)
        }

        System.err.println("[BEFORE]" + TestCollector.Companion.C.status())
        TestCollector.Companion.C.open().use { ignored ->
            TestCollector.Companion.D.open().use { ignored2 ->
                System.err.println("[ACTIVE]" + TestCollector.Companion.C.status() + TestCollector.Companion.D.status())
                println("c " + TestCollector.Companion.C.apply("one"))
                println("c " + TestCollector.Companion.C.apply("two"))
                println("c " + TestCollector.Companion.C.apply("three"))
                println("c " + TestCollector.Companion.C.apply("four"))
                println("d " + TestCollector.Companion.D.apply("one"))
                println("d " + TestCollector.Companion.D.apply("two"))
                println("d " + TestCollector.Companion.D.apply("three"))
                println("d " + TestCollector.Companion.D.apply("four"))
            }
        }
        System.err.println("[AFTER]" + TestCollector.Companion.C.status())
        try {
            println("c " + TestCollector.Companion.C.apply("one"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("c " + TestCollector.Companion.C.apply("two"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("c " + TestCollector.Companion.C.apply("three"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("c " + TestCollector.Companion.C.apply("four"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + TestCollector.Companion.D.apply("one"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + TestCollector.Companion.D.apply("two"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + TestCollector.Companion.D.apply("three"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
        try {
            println("d " + TestCollector.Companion.D.apply("four"))
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun test2Fail() {
        for (e in TestCollector.Companion.SET) {
            TestCollector.Companion.C.add(e)
            TestCollector.Companion.D.add(e)
        }

        System.err.println("[BEFORE]" + TestCollector.Companion.C.status())
        TestCollector.Companion.C.open().use { ignored ->
            TestCollector.Companion.D.open().use { ignored2 ->
                System.err.println("[ACTIVE]" + TestCollector.Companion.C.status() + TestCollector.Companion.D.status())
                println("c " + TestCollector.Companion.C.apply("one"))
                println("c " + TestCollector.Companion.C.apply("two"))
                println("c " + TestCollector.Companion.C.apply("three"))
                println("c " + TestCollector.Companion.C.apply("four"))
                println("d " + TestCollector.Companion.D.apply("one"))
                println("d " + TestCollector.Companion.D.apply("two"))
                println("d " + TestCollector.Companion.D.apply("three"))
                println("d " + TestCollector.Companion.D.apply("four"))
            }
        }
        System.err.println("[AFTER]" + TestCollector.Companion.C.status())
        println("c " + TestCollector.Companion.C.apply("one"))
    }

    companion object {

        private val SET = mutableSetOf<String?>("one", "two", "three")

        private val COMPARATOR: Comparator<String?> = Comparator.naturalOrder<String?>()

        private val C = SetCollector<String?>(TestCollector.Companion.COMPARATOR)

        private val D = SetCollector<String?>(TestCollector.Companion.COMPARATOR)
    }
}
