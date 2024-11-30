package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.AnnoSetType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable

class FEGroupPattern_AnnoSet private constructor(
    pattern: FEGroupPattern,
    annosetid: Int,
) : Pair<FEGroupPattern, Int>(pattern, annosetid), Insertable {

    // I N S E R T A B L E

    @RequiresIdFrom(type = FEGroupPattern::class)
    override fun dataRow(): String {
        return "${first.getSqlId()},$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[PAT-AS pattern=$first annosetid=$second]"
    }

    companion object {

        @JvmField
        val SET = HashSet<FEGroupPattern_AnnoSet>()

        @JvmStatic
        fun make(pattern: FEGroupPattern, annoset: AnnoSetType): FEGroupPattern_AnnoSet {
            val pa = FEGroupPattern_AnnoSet(pattern, annoset.getID())
            SET.add(pa)
            return pa
        }
    }
}
