package org.semantikos.fn.joins

import edu.berkeley.icsi.framenet.AnnoSetType
import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.fn.objects.Governor
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

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Governor_AnnoSet
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

        val COMPARATOR: Comparator<Governor_AnnoSet> = compareBy(Governor.COMPARATOR, Governor_AnnoSet::governor)
            .thenBy { it.annosetid }

        val SET = HashSet<Governor_AnnoSet>()

        fun make(governor: Governor, annoset: AnnoSetType): Governor_AnnoSet {
            val ga = Governor_AnnoSet(governor, annoset.getID())
            SET.add(ga)
            return ga
        }
    }
}
