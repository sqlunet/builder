package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.nullableQuotedString

class Func private constructor(funcName: String) : HasId, Comparable<Func>, Insertable {

    val func: String = normalize(funcName)

    // N I D

    @RequiresIdFrom(type = Func::class)
    override fun getIntId(): Int {
        return COLLECTOR.apply(this)
    }

    // O R D E R

    override fun compareTo(that: Func): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$func',${nullableQuotedString<String>(PREDEFINED[func])}"
    }

    override fun toString(): String {
        return "f:$func"
    }

    companion object {

        val COMPARATOR: Comparator<Func> = Comparator.comparing<Func, String> { it.func }

        @JvmField
        val COLLECTOR = SetCollector<Func>(COMPARATOR)

        private val PREDEFINED = mapOf(
            "ADJ" to "adjectival",
            "ADV" to "adverbial",
            "CAU" to "cause",
            "COM" to "comitative",
            "CXN" to "constructional pattern (adjectival comparative marker)",
            "DIR" to "directional",
            "DIS" to "discourse marker",
            "DSP" to "direct speech",
            "EXT" to "extent",
            "GOL" to "goal",
            "LOC" to "location",
            "LVB" to "light verb (for nouns,",
            "MNR" to "manner",
            "MOD" to "modal",
            "NEG" to "negation",
            "PAG" to "prototypical agent (for arg1,",
            "PNC" to "purpose no cause (deprecated,",
            "PPT" to "prototypical patient (for arg1,",
            "PRD" to "secondary predication",
            "PRP" to "purpose ",
            "PRR" to "nominal predicates in light verb constructions",
            "RCL" to "relative clause (deprecated,",
            "REC" to "reciprocal",
            "SLC" to "selectional constraint link",
            "TMP" to "temporal",
            "VSP" to "verb specific (for nouns,",

            "SE1" to "1st spatial entity",
            "SE2" to "2nd spatial entity",
            "SE3" to "3rd spatial entity",
            "SE4" to "4th spatial entity",
            "SE5" to "5th spatial entity",
            "SE6" to "6th spatial entity",

            "ANC" to "anchor",
            "ANC1" to "1st anchor",
            "ANC2" to "2nd anchor",
            "ANG" to "angle",
            "AXS" to "axis",
            "AXSp" to "perpendicular axis",
            "AXSc" to "central axis",
            "AXS1" to "axis of 1st spatial entity",
            "AXS2" to "axis of 2nd spatial entity",
            "PLN" to "plane",
            "PLN1" to "plane of 1st spatial entity",
            "PLN2" to "plane of 2nd spatial entity",
            "PSN" to "position",
            "TOP" to "top",
            "ORT" to "orientation",
            "SRC" to "source point",
            "SCL" to "scale",

            "DOM" to "domain",
            "WHL" to "whole spatial entity",
            "PRT" to "part",
            "PRT1" to "1st part",
            "PRT2" to "2nd part",
            "SET" to "set",
            "SEQ" to "sequence of individual units",
        )

        @JvmStatic
        fun make(f: String): Func {
            if (f == "" || f == "_") {
                print("'$f'")
            }
            val func = Func(f.uppercase().trim())
            COLLECTOR.add(func)
            return func
        }

        @JvmStatic
        fun makeOrNull(f: String): Func? {
            if (f == "" || f == "_")
                return null
            return make(f)
        }

        private fun normalize(funcName: String): String {
            for (k in PREDEFINED.keys) {
                if (k.equals(funcName, ignoreCase = true)) {
                    return k
                }
            }
            return funcName.lowercase()
        }

        @RequiresIdFrom(type = Func::class)
        fun getIntId(func: Func): Int {
            return func.intId
        }
    }
}
