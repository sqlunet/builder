package org.semantikos.pb31

import org.junit.Assert
import org.junit.Test
import org.semantikos.common.Names

class TestNames {

    val tables = arrayOf(
        "argtypes",
        "args",
        "aspects",
        "examples",
        "forms",
        "funcs",
        "persons",
        "rels",
        "roles",
        "members",
        "rolesets",
        "tenses",
        "thetas",
        "voices",
        "words",
        "pbrolesets_fnframes",
        "pbrolesets_vnclasses",
        "pbroles_vnroles"
    )

    @Test
    fun testNames() {
        val names = Names("pb31")
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
