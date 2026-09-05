package org.semantikos.su.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.Resolvable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils.quotedEscapedString
import java.io.Serializable
import java.util.*

typealias SUFormula = org.sigma.core.Formula

class Formula private constructor(
    val formula: SUFormula, val file: SUFile,
) : HasId, Insertable, Serializable, Comparable<Formula>, Resolvable<String, Int> {

    val formulaText: String
        get() = formula.form

    fun getFile(): String {
        return file.filename
    }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Formula
        return formula.form == that.formula.form
    }

    override fun hashCode(): Int {
        return Objects.hash(formula)
    }

    // O R D E R

    override fun compareTo(other: Formula): Int {
        return COMPARATOR.compare(this, other)
    }

    // T O S T R I N G

    override fun toString(): String {
        return formula.form
    }

    fun toShortString(ellipsizeAfter: Int): String {
        if (formula.form.length > ellipsizeAfter) {
            return formula.form.substring(0, ellipsizeAfter) + "..."
        }
        return formula.form
    }

    // I N S E R T

    override fun dataRow(): String {
        return "${resolve()},${quotedEscapedString(toString())},${resolveFile(file)}"
    }

    override fun comment(): String {
        return file.filename
    }

    // R E S O L V E

    fun resolve(): Int {
        return intId
    }

    fun resolveFile(file: SUFile): Int {
        return file.resolve()
    }

    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    override fun resolving(): String {
        return formula.form
    }

    companion object {

        val COMPARATOR: Comparator<Formula> = Comparator
            .comparing<Formula, String> { it.formulaText }

        val COLLECTOR = SetCollector<Formula>(COMPARATOR)

        fun make(formula: org.sigma.core.Formula): Formula {
            val filename = formula.getSourceFile()
            val f = Formula(formula, SUFile.make(filename))
            COLLECTOR.add(f)
            return f
        }
    }
}
