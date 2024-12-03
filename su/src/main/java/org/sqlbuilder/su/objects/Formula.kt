package org.sqlbuilder.su.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.quotedEscapedString
import java.io.Serializable
import java.util.*

typealias SUFormula = com.articulate.sigma.Formula

class Formula private constructor(
    @JvmField val formula: SUFormula, val file: SUFile,
) : HasId, Insertable, Serializable, Comparable<Formula>, Resolvable<String, Int> {

    val formulaText: String
        get() = formula.form

    fun getFile(): String {
        return file.filename
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Formula
        return formula.form == that.formula.form
    }

    override fun hashCode(): Int {
        return Objects.hash(formula)
    }

    // O R D E R

    override fun compareTo(that: Formula): Int {
        return COMPARATOR.compare(this, that)
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
        return COLLECTOR.apply(this)
    }

    override fun resolving(): String {
        return formula.form
    }

    companion object {

        val COMPARATOR: Comparator<Formula> = Comparator
            .comparing<Formula, String> { it.formulaText }

        @JvmField
        val COLLECTOR = SetCollector<Formula>(COMPARATOR)

        @JvmStatic
        fun make(formula: com.articulate.sigma.Formula): Formula {
            val filename = formula.getSourceFile()
            val f = Formula(formula, SUFile.make(filename))
            COLLECTOR.add(f)
            return f
        }
    }
}
