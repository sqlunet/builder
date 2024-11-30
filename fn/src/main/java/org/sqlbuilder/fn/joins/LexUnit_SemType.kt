package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.SemTypeRefType
import org.sqlbuilder.common.Insertable

class LexUnit_SemType private constructor(
    luid: Int, semtypeid: Int,
) : Pair<Int, Int>(luid, semtypeid), Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$first,$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[LU-SEM luid=$first semtypeid=$second]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<LexUnit_SemType> = Comparator
            .comparing<LexUnit_SemType, Int> { it.first }
            .thenComparing<Int> { it.second }

        @JvmField
        val SET = HashSet<LexUnit_SemType>()

        // C O N S T R U C T O R
        @JvmStatic
        fun make(luid: Int, semtype: SemTypeRefType): LexUnit_SemType {
            val ut = LexUnit_SemType(luid, semtype.getID())
            SET.add(ut)
            return ut
        }
    }
}
