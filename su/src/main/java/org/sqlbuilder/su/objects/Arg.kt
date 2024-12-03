package org.sqlbuilder.su.objects

import org.sqlbuilder.common.Insertable
import java.io.Serializable

/**
 * This class encapsulates what relates a token in a logical statement to the entire statement. The type is arg when the term is nested only within one pair of
 * parentheses. The other possible types are "ant" for rule antecedent, "cons" for rule consequent, and "stmt" for cases where the term is nested inside
 * multiple levels of parentheses. argumentNum is only meaningful when the type is "arg"
 */
class Arg(
    val isInAntecedent: Boolean,
    val isInConsequent: Boolean,
    argumentNum: Int,
    parenLevel: Int,
) : Insertable, Serializable {

    val isArg: Boolean = !isInAntecedent && !isInConsequent && parenLevel == 1

    val isStatement: Boolean = !isInAntecedent && !isInConsequent && parenLevel > 1

    @JvmField
    val argumentNum: Int = if (isArg) argumentNum else -1

    val type: String
        get() {
            if (isInAntecedent) {
                return "p"
            } else if (isInConsequent) {
                return "c"
            } else if (isArg) {
                return "a"
            } else if (isStatement) {
                return "s"
            }
            throw IllegalArgumentException(toString())
        }

    /**
     * Check
     *
     * @throws IllegalArgumentException illegal argument
     */
    fun check() {
        if (isInAntecedent) {
            return
        } else if (isInConsequent) {
            return
        } else if (isArg) {
            return
        } else if (isStatement) {
            return
        }
        throw IllegalArgumentException(toString())
    }

    // T O   S T R I N G

    override fun toString(): String {
        if (isInAntecedent) {
            return "ant"
        } else if (isInConsequent) {
            return "cons"
        } else if (isArg) {
            return "arg-$argumentNum"
        } else if (isStatement) {
            return "stmt"
        }
        return "ILLEGAL"
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$type',${if (isArg) argumentNum else "NULL"}"
    }

    override fun comment(): String {
        return toString()
    }
}
