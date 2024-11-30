package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.InternalFrameRelationFEType
import org.sqlbuilder.common.Insertable

class FE_FERequired private constructor(
    feid: Int,
    feid2: Int,
) : Pair<Int, Int>(feid, feid2), Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$first,$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FE-reqFE feid=$first feid2=$second]"
    }

    companion object {

        @JvmField
        val SET = HashSet<FE_FERequired>()

        // C O N S T R U C T O R
        @JvmStatic
        fun make(fe: Int, fe2: InternalFrameRelationFEType): FE_FERequired {
            val ff = FE_FERequired(fe, fe2.getID())
            SET.add(ff)
            return ff
        }

        // O R D E R
        @JvmField
        val COMPARATOR: Comparator<FE_FERequired> = Comparator
            .comparing<FE_FERequired, Int> { it.getFirst() }
            .thenComparing<Int> { it.getSecond() }
    }
}
