package org.sqlbuilder.fn.joins

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.ValenceUnit

class ValenceUnit_AnnoSet private constructor(
    vu: ValenceUnit,
    annosetid: Int,
) : Pair<ValenceUnit, Int>(vu, annosetid), Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "${first.intId},$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[VU-AS vu=$first annoset=$second]"
    }

    companion object {

        @JvmField
        val SET = HashSet<ValenceUnit_AnnoSet>()

        @JvmStatic
        fun make(vu: ValenceUnit, annosetid: Int): ValenceUnit_AnnoSet {
            val va = ValenceUnit_AnnoSet(vu, annosetid)
            SET.add(va)
            return va
        }
    }
}
