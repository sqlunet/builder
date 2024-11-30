package org.sqlbuilder.common

import org.junit.BeforeClass
import org.junit.Test
import org.sqlbuilder.common.Variables.Companion.make
import java.util.*

class TestVariables {

    @Test
    fun testVariablesWithDollar() {
        testVariables(dollarVs)
    }

    @Test
    fun testVariablesWithAt() {
        testVariables(atVs)
    }

    fun testVariables(vs: Array<String>) {
        for (v in vs) {
            try {
                val w: String = variables!!.varSubstitution(v, true)
                println(w)
            } catch (_: IllegalArgumentException) {
                System.err.println("Not found: $v")
            }
        }
    }

    companion object {

        private val dollarVs = arrayOf<String>("\${var.a}", "\${var.b}", "\${var.c}", "__\${var.a}__", "__\${var.b}__", "__\${var.c}__", "\${var.z}")
        private val atVs = arrayOf<String>("@{var.a}", "@{var.b}", "@{var.c}", "__@{var.a}__", "__@{var.b}__", "__@{var.c}__", "@{var.z}")

        private var variables: Variables? = null

        @BeforeClass
        @JvmStatic
        fun init() {
            val bundle = ResourceBundle.getBundle("Names")
            variables = make(bundle)
        }
    }
}
