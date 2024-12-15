package org.semantikos.fn

import org.junit.Assert
import org.junit.Test
import org.semantikos.common.Names

class TestNames {

    val tables = arrayOf(
        "annosets",
        "coretypes",
        "corpuses",
        "cxns",
        "documents",
        "fes",
        "fetypes",
        "fes_semtypes",
        "fes_excluded",
        "fes_required",
        "ferealizations",
        "ferealizations_valenceunits",
        "fegrouprealizations",
        "fes_fegrouprealizations",
        "frames",
        "frames_related",
        "frames_related",
        "framerelations",
        "frames_semtypes",
        "gftypes",
        "governors",
        "governors_annosets",
        "grouppatterns",
        "grouppatterns_patterns",
        "grouppatterns_annosets",
        "labelitypes",
        "labels",
        "labeltypes",
        "layers",
        "layertypes",
        "lexemes",
        "lexunits",
        "lexunits_governors",
        "lexunits_semtypes",
        "poses",
        "pttypes",
        "semtypes",
        "semtypes_supers",
        "sentences",
        "statuses",
        "subcorpuses",
        "subcorpuses_sentences",
        "valenceunits",
        "valenceunits_annosets",
        "words"
    )

    @Test
    fun testNames() {
        val names = Names("fn")
        for (key in tables) {
            val f = names.file(key)
            val t = names.table(key)
            val c = names.columns(key)
            println("$key - $f $t $c")
            Assert.assertEquals("$key.sql", f)
            Assert.assertEquals("`fn_$key`", t)
        }
    }
}
