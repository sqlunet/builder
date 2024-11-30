package org.sqlbuilder.fn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.Governor

class LexUnit_Governor private constructor(
    luid: Int, governor: Governor,
) : Pair<Int, Governor>(luid, governor), Insertable {

    // I N S E R T

    @RequiresIdFrom(type = Governor::class)
    override fun dataRow(): String {
        return "$first,${second.getSqlId()}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[LU-GOV lu=$first governor=$second]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<LexUnit_Governor> = Comparator
            .comparing<LexUnit_Governor, Int> { it.first }
            .thenComparing<Governor>({ it.second }, Governor.COMPARATOR)

        @JvmField
        val SET = HashSet<LexUnit_Governor>()

        @JvmStatic
        fun make(luid: Int, governor: Governor): LexUnit_Governor {
            val ug = LexUnit_Governor(luid, governor)
            SET.add(ug)
            return ug
        }
    }
}
