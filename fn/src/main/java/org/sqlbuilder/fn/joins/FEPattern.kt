package org.sqlbuilder.fn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.FERealization
import org.sqlbuilder.fn.objects.ValenceUnit
import java.util.Objects

data class FEPattern(
    val fer: FERealization,
    val vu: ValenceUnit,
) : Insertable {

    val feName: String
        get() = fer.fEName

    val luId: Int
        get() = fer.luId

    // I N S E R T

    @RequiresIdFrom(type = FERealization::class)
    @RequiresIdFrom(type = ValenceUnit::class)
    override fun dataRow(): String {
        return "${fer.getSqlId()},${vu.getSqlId()}"
    }

    override fun comment(): String {
        return "luid=${fer.luId},fe=${fer.fEName},vu={${vu.fE},${vu.pT},${vu.gF}}"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as FEPattern
        return fer == that.fer && vu == that.vu
    }

    override fun hashCode(): Int {
        return Objects.hash(fer, vu)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FER-VU fer=$fer vu=$vu]"
    }

    companion object {

        val COMPARATOR: Comparator<FEPattern> = Comparator
            .comparing<FEPattern, Int> { it.luId }
            .thenComparing<String> { it.feName }
            .thenComparing<ValenceUnit> { it.vu }

        val SET = HashSet<FEPattern>()

        fun make(fer: FERealization, vu: ValenceUnit): FEPattern {
            val vr = FEPattern(fer, vu)
            SET.add(vr)
            return vr
        }
    }
}
