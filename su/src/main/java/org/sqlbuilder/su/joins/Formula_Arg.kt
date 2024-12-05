package org.sqlbuilder.su.joins

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.su.FormulaParser
import org.sqlbuilder.su.objects.Arg
import org.sqlbuilder.su.objects.Formula
import org.sqlbuilder.su.objects.Term
import org.sqlbuilder.su.objects.Term.Companion.make
import java.io.IOException
import java.io.Serializable
import java.text.ParseException

class Formula_Arg private constructor(
    val formula: Formula,
    val term: Term,
    @JvmField val arg: Arg,
) : Insertable, Serializable, Comparable<Formula_Arg> {

    val argNum: Int
        get() = arg.argumentNum

    // O R D E R

    override fun compareTo(that: Formula_Arg): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "${resolveFormula(formula)},${resolveTerm(term)},${arg.dataRow()}"
    }

    override fun comment(): String {
        return "${term.term}, ${formula.toShortString(32)}"
    }

    // R E S O L V E

    fun resolve(): Int {
        return -1
    }

    fun resolveTerm(term: Term): Int {
        return term.resolve()
    }

    fun resolveFormula(formula: Formula): Int {
        return formula.resolve()
    }

    companion object {

        private val COMPARATOR: Comparator<Formula_Arg> = Comparator
            .comparing<Formula_Arg, Int> { it.argNum }

        @Throws(IllegalArgumentException::class, ParseException::class, IOException::class)
        fun make(formula: Formula): Collection<Formula_Arg> {
            return FormulaParser.parse(formula.formula)
                .map {
                    val key: String = it.key
                    val term = make(key)
                    val parse: Arg = it.value
                    Formula_Arg(formula, term, parse)
                }
                .sorted()
                .toList()
        }
    }
}
