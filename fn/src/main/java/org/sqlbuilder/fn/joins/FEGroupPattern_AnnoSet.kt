package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.AnnoSetType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import java.util.*

data class FEGroupPattern_AnnoSet(
    val pattern: FEGroupPattern,
    val annosetid: Int,
) : Insertable {

    // I N S E R T A B L E

    @RequiresIdFrom(type = FEGroupPattern::class)
    override fun dataRow(): String {
        return "${pattern.getSqlId()},$annosetid"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as FEGroupPattern_AnnoSet
        return pattern == that.pattern && annosetid == that.annosetid
    }

    override fun hashCode(): Int {
        return Objects.hash(pattern, annosetid)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[PAT-AS pattern=$pattern annosetid=$annosetid]"
    }

    companion object {

        val SET = HashSet<FEGroupPattern_AnnoSet>()

        fun make(pattern: FEGroupPattern, annoset: AnnoSetType): FEGroupPattern_AnnoSet {
            val pa = FEGroupPattern_AnnoSet(pattern, annoset.getID())
            SET.add(pa)
            return pa
        }
    }
}
