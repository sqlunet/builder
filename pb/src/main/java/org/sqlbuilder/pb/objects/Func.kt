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

        private val PREDEFINED = arrayOf("ADJ", "ADV", "AV", "CAU", "COM", "DIR", "DIS", "DSP", "EXT", "GOL", "LOC", "MNR", "MOD", "NEG", "PNC", "PRD", "PRED", "PRR", "Q", "RCL", "REC", "SLC", "STR", "TMP")
        //ANC | ANC1 | ANC2 | ANG | DOM | AXS | AXSx | AXSy | AXSz | AXSp | AXSc | AXS1 | AXS2 | ORT | ORGN | WHL | SEQ | SET | SRC | SE1 | SE2 | SE3 | SE4 | SE5 | SE6 | SE7 | SE8 | SE9 | SCL | PAG | PLN | PLN1 | PLN2 | PPT | PRT | PRT1 | PRT2 | GOL | CXN | VSP | COM | ADJ | CAU | PRP | MNR | EXT | LOC | REC | DIR | ADV | TMP | adv | tmp | pag | ppt | gol | vsp | com | adj | cau | prp | rec | mnr | ext | loc | dir | prd | PRD

        /*
        FOUND
        -----
        -
        ADJ
        ADV
        ANC
        ANC1
        ANC2
        ANG
        AXS
        AXS1
        AXS2
        AXSc
        AXSp
        CAU
        COM
        DIR
        DOM
        DOM
        EXT
        GOL
        LOC
        MNR
        ORT
        pag
        PAG
        PLN
        ppt
        PPT
        PRD
        PRP
        PRT
        PRT1
        PRT2
        REC
        SCL
        SE1
        SE2
        SE3
        SET
        SRC
        TMP
        VSP
        WHL
        */

        /*
        roles have a number (or an "M" associated
        with them, for common adjuncts that don't qualify for number argument status).
        Both numbered arguments and adjuncts are labeled with the function tags from the list below:

        EXT
        LOC
        DIR
        NEG
        MOD
        ADV
        MNR
        PRD
        REC
        TMP
        PRP
        PNC
        CAU
        CXN
        ADJ
        COM
        DIS
        DSP
        GOL
        PAG
        PPT
        RCL
        SLC
        VSP
        LVB

        // Function tags for spatial arguments
        SE1  first thing (UNVERIFIED)
        SE2  second thing (UNVERIFIED)
        SE3  third thing (UNVERIFIED)
        SE4  fourth thing (UNVERIFIED)
        SE5  fifth thing (UNVERIFIED)
        SE6  sixth thing (UNVERIFIED)

        SCL  Scale (UNVERIFIED)

        DOM  Domain (UNVERIFIED)

        # Added from the spatial relations ontology https://docs.google.com/spreadsheets/d/1b_CJUsyGptG0RtZQ5qnuJjF0wHKyiahLJ83RhHRwgCE/edit#gid=154847108

        ANC  anchor
        ANC1 first anchor
        ANC2 second anchor

        ANG angle

        AXS  axis
        AXSp perpendicular axis
        AXSc central axis
        AXS1 axis of spatial entity 1
        AXS2 axis of spatial entity 2

        WHL  whole spatial entity

        SEQ  sequence

        PSN  position

        // Others
        SET
        SRC  Source (UNVERIFIED)
        PRT
        */

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
            DESCRIPTIONS.setProperty("MOD", "general modification")
            DESCRIPTIONS.setProperty("NEG", "negation")
            DESCRIPTIONS.setProperty("PAG", "purpose no cause (deprecated)")
            DESCRIPTIONS.setProperty("PAG", "prototypical agent (for arg1)")
            DESCRIPTIONS.setProperty("PPT", "prototypical patient (for arg1)")
            DESCRIPTIONS.setProperty("PRD", "secondary predication")
            DESCRIPTIONS.setProperty("PRP", "purpose ")
            DESCRIPTIONS.setProperty("PRR", "Nominal predicates in light verb constructions")
            DESCRIPTIONS.setProperty("Q", "quantity")
            DESCRIPTIONS.setProperty("RCL", "relative clause (deprecated)")
            DESCRIPTIONS.setProperty("REC", "reciprocal")
            DESCRIPTIONS.setProperty("SLC", "selectional constraint link")
            DESCRIPTIONS.setProperty("TMP", "temporal")
            DESCRIPTIONS.setProperty("VSP", "verb specific (for nouns)")
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
