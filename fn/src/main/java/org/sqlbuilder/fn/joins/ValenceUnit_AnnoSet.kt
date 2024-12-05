package org.sqlbuilder.fn.joins

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.ValenceUnit
import java.util.*

data class ValenceUnit_AnnoSet(
    val vu: ValenceUnit,
    val annosetid: Int,
) : Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "${vu.intId},$annosetid"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as ValenceUnit_AnnoSet
        return vu == that.vu && annosetid == that.annosetid
    }

    override fun hashCode(): Int {
        return Objects.hash(vu, annosetid)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[VU-AS vu=$vu annoset=$annosetid]"
    }

    companion object {

        val SET = HashSet<ValenceUnit_AnnoSet>()

        fun make(vu: ValenceUnit, annosetid: Int): ValenceUnit_AnnoSet {
            val va = ValenceUnit_AnnoSet(vu, annosetid)
            SET.add(va)
            return va
        }
    }
}
