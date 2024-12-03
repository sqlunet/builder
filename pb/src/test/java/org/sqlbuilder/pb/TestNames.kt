package org.sqlbuilder.pb

import org.junit.Assert
import org.junit.Test
import org.sqlbuilder.common.Names

class TestNames {

    val tables = arrayOf(
        "rolesets",
        "roles",
        "members",
        "words",
        "argtypes",
        "funcs",
        "vnroles",
        "fnfes",
        "examples",
        "rels",
        "args",
        "pbrolesets_vnclasses",
        "pbroles_vnroles",
        "pbrolesets_fnframes",
        "pbroles_fnfes",
    )

    @Test
    fun testNames() {
        val names = Names("pb")
        for (key in tables) {
            val f = names.file(key)
            val t = names.table(key)
            val c = names.columns(key)
            println("$key - $f $t $c")
            Assert.assertEquals("$key.sql", f)
            Assert.assertEquals("`pb_$key`", t)
        }
    }
}
