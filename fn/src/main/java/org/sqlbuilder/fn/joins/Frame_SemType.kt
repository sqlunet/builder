package org.sqlbuilder.fn.joins

import org.sqlbuilder.common.Insertable

class Frame_SemType private constructor(
    frameid: Int,
    semtypeid: Int,
) : Pair<Int, Int>(frameid, semtypeid), Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$first,$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FR-SEM frameid=$first semtypeid=$second]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<Frame_SemType> = Comparator
            .comparing<Frame_SemType, Int> { it.first }
            .thenComparing<Int> { it.second }

        @JvmField
        val SET = HashSet<Frame_SemType>()

        @JvmStatic
        fun make(frameid: Int, semtypeid: Int): Frame_SemType {
            val ft = Frame_SemType(frameid, semtypeid)
            SET.add(ft)
            return ft
        }
    }
}
