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
        return COLLECTOR[this]!!
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

        private val PREDEFINED = arrayOf("ADV", "AV", "CAU", "DIR", "DIS", "DS", "DSP", "EXT", "LOC", "MNR", "MOD", "NEG", "PNC", "PRD", "PRED", "PRP", "Q", "RCL", "REC", "SLC", "STR", "TMP")
        //ANC | ANC1 | ANC2 | ANG | DOM | AXS | AXSx | AXSy | AXSz | AXSp | AXSc | AXS1 | AXS2 | ORT | ORGN | WHL | SEQ | SET | SRC | SE1 | SE2 | SE3 | SE4 | SE5 | SE6 | SE7 | SE8 | SE9 | SCL | PAG | PLN | PLN1 | PLN2 | PPT | PRT | PRT1 | PRT2 | GOL | CXN | VSP | COM | ADJ | CAU | PRP | MNR | EXT | LOC | REC | DIR | ADV | TMP | adv | tmp | pag | ppt | gol | vsp | com | adj | cau | prp | rec | mnr | ext | loc | dir | prd | PRD

        private val DESCRIPTIONS = Properties()

        init {
            DESCRIPTIONS.setProperty("ADV", "adverbial modification")
            DESCRIPTIONS.setProperty("CAU", "cause")
            DESCRIPTIONS.setProperty("DIR", "direction")
            DESCRIPTIONS.setProperty("EXT", "extent")
            DESCRIPTIONS.setProperty("LOC", "location")
            DESCRIPTIONS.setProperty("MNR", "manner")
            DESCRIPTIONS.setProperty("MOD", "general modification")
            DESCRIPTIONS.setProperty("NEG", "negation")
            DESCRIPTIONS.setProperty("PNC", "purpose no cause")
            DESCRIPTIONS.setProperty("PRD", "secondary predication")
            DESCRIPTIONS.setProperty("PRP", "purpose (deprecated)")
            DESCRIPTIONS.setProperty("Q", "quantity")
            DESCRIPTIONS.setProperty("RCL", "relative clause")
            DESCRIPTIONS.setProperty("REC", "reciprocal")
            DESCRIPTIONS.setProperty("TMP", "temporal")
        }

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
            return COLLECTOR[func]!!
        }
    }
}
