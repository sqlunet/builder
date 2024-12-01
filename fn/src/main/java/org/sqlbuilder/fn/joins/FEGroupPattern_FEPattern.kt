package org.sqlbuilder.fn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.FERealization
import org.sqlbuilder.fn.objects.ValenceUnit

class FEGroupPattern_FEPattern private constructor(
    groupPattern: FEGroupPattern,
    fer: FERealization,
    vu: ValenceUnit,
) : Triple<FEGroupPattern, FERealization, ValenceUnit>(groupPattern, fer, vu), Insertable {

    // I N S E R T

    @RequiresIdFrom(type = FEGroupPattern::class)
    @RequiresIdFrom(type = FERealization::class)
    @RequiresIdFrom(type = ValenceUnit::class)
    override fun dataRow(): String {
        return "${first.getSqlId()},${second.getSqlId()},${third.getSqlId()}"
    }

    override fun comment(): String {
        return "${first.comment()} ${second.comment()} vu={${third.comment()}}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[PAT-VU fer=$second pattern=$first, vu=$third]"
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
