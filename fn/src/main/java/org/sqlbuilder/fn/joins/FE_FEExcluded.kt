package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.InternalFrameRelationFEType
import org.sqlbuilder.common.Insertable
import java.util.Objects

data class FE_FEExcluded(
    val feid: Int,
    val feid2: Int,
) : Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$feid,$feid2"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as FE_FEExcluded
        return feid == that.feid && feid2 == that.feid2
    }

    override fun hashCode(): Int {
        return Objects.hash(feid, feid2)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FE-exclFE feid=$feid feid2=$feid2]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<FE_FEExcluded> = Comparator
            .comparing<FE_FEExcluded, Int> { it.feid }
            .thenComparing<Int?> { it.feid2 }

        @JvmField
        val SET = HashSet<FE_FEExcluded>()

        fun make(feid: Int, fe2: InternalFrameRelationFEType): FE_FEExcluded {
            val ff = FE_FEExcluded(feid, fe2.getID())
            SET.add(ff)
            return ff
        }
    }
}
