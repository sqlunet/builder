package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.AnnoSetType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.Governor
import java.util.*

data class Governor_AnnoSet(
    val governor: Governor,
    val annosetid: Int,
) : Insertable {

    // I N S E R T

    @RequiresIdFrom(type = Governor::class)
    override fun dataRow(): String {
        return "${governor.getSqlId()},$annosetid"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Governor_AnnoSet
        return governor == that.governor && annosetid == that.annosetid
    }

    override fun hashCode(): Int {
        return Objects.hash(governor, annosetid)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[GOV-AS governor=$governor annosetid=$annosetid]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<Governor_AnnoSet> = Comparator
            .comparing<Governor_AnnoSet, Governor>({ it.governor }, Governor.COMPARATOR)
            .thenComparing<Int> { it.annosetid }

        @JvmField
        val SET = HashSet<Governor_AnnoSet>()

        fun make(governor: Governor, annoset: AnnoSetType): Governor_AnnoSet {
            val ga = Governor_AnnoSet(governor, annoset.getID())
            SET.add(ga)
            return ga
        }
    }
}
