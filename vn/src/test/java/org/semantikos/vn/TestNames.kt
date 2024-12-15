package org.semantikos.vn

import org.junit.Assert
import org.junit.Test
import org.semantikos.common.Names

class TestNames {

    val tables: Array<String> = arrayOf<String>(
        "classes",
        "members",
        "members_senses",
        "groupings",
        "members_groupings",
        "restrtypes",
        "restrs",
        "roletypes",
        "roles",
        "classes_frames",
        "frames",
        "framenames",
        "framesubnames",
        "examples",
        "frames_examples",
        "semantics",
        "predicates",
        "predicates_semantics",
        "syntaxes",
        "words"
    )

    @Test
    fun testNames() {
        val names = Names("vn")
        for (key in tables) {
            val f = names.file(key)
            val t = names.table(key)
            val c = names.columns(key)
            println("$key - $f $t $c")
            Assert.assertEquals("$key.sql", f)
            Assert.assertEquals("`vn_$key`", t)
        }
    }
}
