package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.InternalFrameRelationFEType
import org.sqlbuilder.common.Insertable

class FE_FEExcluded private constructor(
    feid: Int,
    feid2: Int,
) : Pair<Int, Int>(feid, feid2), Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$first,$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FE-exclFE feid=$first feid2=$second]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<FE_FEExcluded> = Comparator
            .comparing<FE_FEExcluded, Int> { it.getFirst() }
            .thenComparing<Int?> { it.getSecond() }

        @JvmField
        val SET = HashSet<FE_FEExcluded>()

        @JvmStatic
        fun make(feid: Int, fe2: InternalFrameRelationFEType): FE_FEExcluded {
            val ff = FE_FEExcluded(feid, fe2.getID())
            SET.add(ff)
            return ff
        }
    }
}
