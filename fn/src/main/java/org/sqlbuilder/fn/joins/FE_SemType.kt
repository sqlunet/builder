package org.sqlbuilder.fn.joins

import org.sqlbuilder.common.Insertable

class FE_SemType private constructor(
    feid: Int,
    semtypeid: Int,
) : Pair<Int, Int>(feid, semtypeid), Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$first,$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FE-SEM feid=$first semtypeid=$second]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<FE_SemType> = Comparator
            .comparing<FE_SemType, Int> { it.getFirst() }
            .thenComparing<Int> { it.getSecond() }

        @JvmField
        val SET = HashSet<FE_SemType>()

        @JvmStatic
        fun make(feid: Int, semtypeid: Int): FE_SemType {
            val fs = FE_SemType(feid, semtypeid)
            SET.add(fs)
            return fs
        }
    }
}
