package org.sqlbuilder.fn.joins

import org.sqlbuilder.common.Insertable

// TODO remove
class Frame_CoreFEMember private constructor(
    frameid: Int,
    feid: Int,
) : Pair<Int, Int>(frameid, feid), Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$first,$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FR-coreFE frameid=$first feid=$second]"
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
