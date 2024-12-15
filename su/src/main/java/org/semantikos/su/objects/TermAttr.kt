package org.semantikos.su.objects

import org.sigma.core.Formula
import org.semantikos.common.NotFoundException
import org.semantikos.common.Utils.nullableQuotedEscapedString
import org.semantikos.su.Kb

class TermAttr private constructor(
    val attr: Char,
) {

    // T O S T R I N G

    override fun toString(): String {
        return attr.toString()
    }

    // I N S E R T

    fun dataRow(): String {
        return nullableQuotedEscapedString(attr.toString())
    }

    companion object {

        private const val ISFUNCTION = 'y'

        private const val ISMATHFUNCTION = 'm'

        private const val ISCOMPARISONOP = '~'

        private const val ISLOGICALOP = 'l'

        private const val ISQUANTIFIER = 'q'

        private const val SUBCLASSOFRELATION = 'R'

        private const val SUBCLASSOFFUNCTION = 'F'

        private const val SUBCLASSOFPREDICATE = 'P'

        private const val SUBCLASSOFATTRIBUTE = 'A'

        private const val CHILDOFRELATION = 'r'

        private const val CHILDOFFUNCTION = 'f'

        private const val CHILDOFPREDICATE = 'p'

        private const val CHILDOFATTRIBUTE = 'a'

        @Throws(NotFoundException::class)
        fun make(sumoTerm: Term, kb: Kb): MutableCollection<TermAttr> {
            val term = sumoTerm.term

            val result: MutableList<TermAttr> = ArrayList<TermAttr>()

            if (Formula.isFunction(term)) {
                result.add(TermAttr(ISFUNCTION))
            }
            if (Formula.isMathFunction(term)) {
                result.add(TermAttr(ISMATHFUNCTION))
            }
            if (Formula.isComparisonOperator(term)) {
                result.add(TermAttr(ISCOMPARISONOP))
            }
            if (Formula.isLogicalOperator(term)) {
                result.add(TermAttr(ISLOGICALOP))
            }
            if (Formula.isQuantifier(term)) {
                result.add(TermAttr(ISQUANTIFIER))
            }

            if (kb.childOf(term, "Relation")) {
                result.add(TermAttr(CHILDOFRELATION))
            }
            if (kb.childOf(term, "Predicate")) {
                result.add(TermAttr(CHILDOFPREDICATE))
            }
            if (kb.childOf(term, "Function")) {
                result.add(TermAttr(CHILDOFFUNCTION))
            }
            if (kb.childOf(term, "Attribute")) {
                result.add(TermAttr(CHILDOFATTRIBUTE))
            }

            if (kb.isSubclass(term, "Relation")) {
                result.add(TermAttr(SUBCLASSOFRELATION))
            }
            if (kb.isSubclass(term, "Predicate")) {
                result.add(TermAttr(SUBCLASSOFPREDICATE))
            }
            if (kb.isSubclass(term, "Function")) {
                result.add(TermAttr(SUBCLASSOFFUNCTION))
            }
            if (kb.isSubclass(term, "Attribute")) {
                result.add(TermAttr(SUBCLASSOFATTRIBUTE))
            }

            if (result.isEmpty()) throw NotFoundException(term)

            return result
        }
    }
}
