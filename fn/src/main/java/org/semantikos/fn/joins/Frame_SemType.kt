package org.semantikos.fn.joins

import org.semantikos.common.Insertable
import java.util.*

data class Frame_SemType(
    val frameid: Int,
    val semtypeid: Int,
) : Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$frameid,$semtypeid"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Frame_SemType
        return frameid == that.frameid && semtypeid == that.semtypeid
    }

    override fun hashCode(): Int {
        return Objects.hash(frameid, semtypeid)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FR-SEM frameid=$frameid semtypeid=$semtypeid]"
    }

    companion object {

        val COMPARATOR: Comparator<Frame_SemType> = Comparator
            .comparing<Frame_SemType, Int> { it.frameid }
            .thenComparing<Int> { it.semtypeid }

        val SET = HashSet<Frame_SemType>()

        fun make(frameid: Int, semtypeid: Int): Frame_SemType {
            val ft = Frame_SemType(frameid, semtypeid)
            SET.add(ft)
            return ft
        }
    }
}
