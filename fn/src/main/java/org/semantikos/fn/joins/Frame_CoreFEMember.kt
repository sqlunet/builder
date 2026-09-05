package org.semantikos.fn.joins

import org.semantikos.common.Insertable
import java.util.*

// TODO remove
class Frame_CoreFEMember private constructor(
    val frameid: Int,
    val feid: Int,
) : Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$frameid,$feid"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Frame_CoreFEMember
        return frameid == that.frameid && feid == that.feid
    }

    override fun hashCode(): Int {
        return Objects.hash(frameid, feid)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FR-coreFE frameid=$frameid feid=$feid]"
    }

    companion object {

        val SET = HashSet<Frame_CoreFEMember>()

        fun make(frameid: Int, feid: Int): Frame_CoreFEMember {
            val fe = Frame_CoreFEMember(frameid, feid)
            SET.add(fe)
            return fe
        }
    }
}
