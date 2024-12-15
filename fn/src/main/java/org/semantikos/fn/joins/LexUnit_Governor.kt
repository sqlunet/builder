package org.semantikos.fn.joins

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.fn.objects.Governor
import java.util.*

data class LexUnit_Governor(
    val luid: Int,
    val governor: Governor,
) : Insertable {

    // I N S E R T

    @RequiresIdFrom(type = Governor::class)
    override fun dataRow(): String {
        return "$luid,${governor.getSqlId()}"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as LexUnit_Governor
        return luid == that.luid && governor == that.governor
    }

    override fun hashCode(): Int {
        return Objects.hash(luid, governor)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[LU-GOV lu=$luid governor=$governor]"
    }

    companion object {

        val COMPARATOR: Comparator<LexUnit_Governor> = Comparator
            .comparing<LexUnit_Governor, Int> { it.luid }
            .thenComparing<Governor>({ it.governor }, Governor.COMPARATOR)

        val SET = HashSet<LexUnit_Governor>()

        fun make(luid: Int, governor: Governor): LexUnit_Governor {
            val ug = LexUnit_Governor(luid, governor)
            SET.add(ug)
            return ug
        }
    }
}
