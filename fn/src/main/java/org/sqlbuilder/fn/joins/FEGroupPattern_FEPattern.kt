package org.sqlbuilder.fn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.FERealization
import org.sqlbuilder.fn.objects.ValenceUnit
import java.util.*

data class FEGroupPattern_FEPattern(
    val groupPattern: FEGroupPattern,
    val fer: FERealization,
    val vu: ValenceUnit,
) : Insertable {

    // I N S E R T

    @RequiresIdFrom(type = FEGroupPattern::class)
    @RequiresIdFrom(type = FERealization::class)
    @RequiresIdFrom(type = ValenceUnit::class)
    override fun dataRow(): String {
        return "${groupPattern.getSqlId()},${fer.getSqlId()},${vu.getSqlId()}"
    }

    override fun comment(): String {
        return "${groupPattern.comment()} ${fer.comment()} vu={${vu.comment()}}"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as FEGroupPattern_FEPattern
        return groupPattern == that.groupPattern && fer == that.fer && vu == that.vu
    }

    override fun hashCode(): Int {
        return Objects.hash(groupPattern, fer, vu)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[PAT-VU fer=$fer pattern=$groupPattern, vu=$vu]"
    }

    companion object {

        @JvmField
        val LIST = ArrayList<FEGroupPattern_FEPattern>()

        @JvmStatic
        fun make(groupPattern: FEGroupPattern, fer: FERealization, vu: ValenceUnit): FEGroupPattern_FEPattern {
            val p = FEGroupPattern_FEPattern(groupPattern, fer, vu)
            LIST.add(p)
            return p
        }
    }
}
