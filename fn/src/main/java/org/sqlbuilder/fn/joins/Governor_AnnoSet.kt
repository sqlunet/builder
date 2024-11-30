package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.AnnoSetType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.Governor

class Governor_AnnoSet private constructor(
    governor: Governor,
    annosetid: Int,
) : Pair<Governor, Int>(governor, annosetid), Insertable {

    // I N S E R T

    @RequiresIdFrom(type = Governor::class)
    override fun dataRow(): String {
        return "${first.getSqlId()},$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[GOV-AS governor=$first annosetid=$second]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<Governor_AnnoSet> = Comparator
            .comparing<Governor_AnnoSet, Governor>({ it.first }, Governor.COMPARATOR)
            .thenComparing<Int> { it.second }

        @JvmField
        val SET = HashSet<Governor_AnnoSet>()

        @JvmStatic
        fun make(governor: Governor, annoset: AnnoSetType): Governor_AnnoSet {
            val ga = Governor_AnnoSet(governor, annoset.getID())
            SET.add(ga)
            return ga
        }
    }
}
