package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils
import java.util.*

class Func private constructor(funcName: String) : HasId, Comparable<Func>, Insertable {

    val func: String = normalize(funcName)

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
        return String.format("'%s',%s", func, Utils.nullableQuotedString<String>(DESCRIPTIONS.getProperty(func, null)))
    }

    override fun toString(): String {
        return "f:$func"
    }

    companion object {

        val COMPARATOR: Comparator<Func> = Comparator.comparing<Func, String> { it.func }

        @JvmField
        val COLLECTOR = SetCollector<Func>(COMPARATOR)

        private val PREDEFINED = arrayOf("ADJ", "ADV", "CAU", "COM", "CXN", "DIR", "DIS", "DSP", "EXT", "GOL", "LOC", "LVB", "MNR", "MOD", "NEG", "PAG", "PNC", "PPT", "PRD", "PRP", "PRR", "RCL", "REC", "SLC", "TMP", "VSP")

        private val DESCRIPTIONS = Properties()

        init {
            DESCRIPTIONS.setProperty("ADJ", "adjectival")
            DESCRIPTIONS.setProperty("ADV", "adverbial")
            DESCRIPTIONS.setProperty("CAU", "cause")
            DESCRIPTIONS.setProperty("COM", "comitative")
            DESCRIPTIONS.setProperty("CXN", "constructional pattern (adjectival comparative marker)")
            DESCRIPTIONS.setProperty("DIR", "directional")
            DESCRIPTIONS.setProperty("DIS", "discourse marker")
            DESCRIPTIONS.setProperty("DSP", "direct speech")
            DESCRIPTIONS.setProperty("EXT", "extent")
            DESCRIPTIONS.setProperty("GOL", "goal")
            DESCRIPTIONS.setProperty("LOC", "location")
            DESCRIPTIONS.setProperty("LVB", "light verb (for nouns)")
            DESCRIPTIONS.setProperty("MNR", "manner")
            DESCRIPTIONS.setProperty("MOD", "modal")
            DESCRIPTIONS.setProperty("NEG", "negation")
            DESCRIPTIONS.setProperty("PAG", "prototypical agent (for arg1)")
            DESCRIPTIONS.setProperty("PNC", "purpose no cause (deprecated)")
            DESCRIPTIONS.setProperty("PPT", "prototypical patient (for arg1)")
            DESCRIPTIONS.setProperty("PRD", "secondary predication")
            DESCRIPTIONS.setProperty("PRP", "purpose ")
            DESCRIPTIONS.setProperty("PRR", "nominal predicates in light verb constructions")
            DESCRIPTIONS.setProperty("RCL", "relative clause (deprecated)")
            DESCRIPTIONS.setProperty("REC", "reciprocal")
            DESCRIPTIONS.setProperty("SLC", "selectional constraint link")
            DESCRIPTIONS.setProperty("TMP", "temporal")
            DESCRIPTIONS.setProperty("VSP", "verb specific (for nouns)")
        }

        /*
         as
         at
         by
         for
         from
         in
         of
         to
         with
         */

        @JvmStatic
        fun make(f: String): Func {
            val fn = Func(f)
            COLLECTOR.add(fn)
            return fn
        }

        private fun normalize(funcName: String): String {
            for (predefined in PREDEFINED) {
                if (predefined.equals(funcName, ignoreCase = true)) {
                    return predefined
                }
            }
            return funcName.lowercase(Locale.getDefault())
        }

        @RequiresIdFrom(type = Func::class)
        fun getIntId(func: Func): Int {
            return COLLECTOR.apply(func)
        }
    }
}
