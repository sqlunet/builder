package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.SemTypeRefType
import org.sqlbuilder.common.Insertable
import java.util.*

data class LexUnit_SemType(
    val luid: Int,
    val semtypeid: Int,
) : Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$luid,$semtypeid"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as LexUnit_SemType
        return luid == that.luid && semtypeid == that.semtypeid
    }

    override fun hashCode(): Int {
        return Objects.hash(luid, semtypeid)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[LU-SEM luid=$luid semtypeid=$semtypeid]"
    }

    companion object {

        val COMPARATOR: Comparator<LexUnit_SemType> = Comparator
            .comparing<LexUnit_SemType, Int> { it.luid }
            .thenComparing<Int> { it.semtypeid }

        val SET = HashSet<LexUnit_SemType>()

        fun make(luid: Int, semtype: SemTypeRefType): LexUnit_SemType {
            val ut = LexUnit_SemType(luid, semtype.getID())
            SET.add(ut)
            return ut
        }
    }
}
