package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.FEValenceType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.FE
import org.sqlbuilder.fn.objects.FEGroupRealization
import org.sqlbuilder.fn.types.FeType.getIntId
import java.util.*

data class FE_FEGroupRealization(
    val feName: String,
    val fegr: FEGroupRealization,
) : Insertable {

    val fENames: String
        get() = fegr.fENames

    // I N S E R T

    @RequiresIdFrom(type = FEGroupRealization::class)
    override fun dataRow(): String {
        val fetypeid: Int = getIntId(feName)!!
        val key = Pair(fetypeid, fegr.frameID)
        val feid = FE.BY_FETYPEID_AND_FRAMEID!![key]!!.iD
        return "${fegr.getSqlId()},$feid,$fetypeid"
    }

    override fun comment(): String {
        return "fe=$feName,fes={$fENames},luid=${fegr.luID},frid=${fegr.frameID}"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as FE_FEGroupRealization
        return feName == that.feName && fegr == that.fegr
    }

    override fun hashCode(): Int {
        return Objects.hash(feName, fegr)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FE-FEGR fe=$feName fegr={$fegr}]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<FE_FEGroupRealization> = Comparator
            .comparing<FE_FEGroupRealization, String> { it.feName }
            .thenComparing<String> { it.fENames }

        @JvmField
        val SET = HashSet<FE_FEGroupRealization>()

        fun make(fe: FEValenceType, fegr: FEGroupRealization): FE_FEGroupRealization {
            val fr = FE_FEGroupRealization(fe.getName(), fegr)
            SET.add(fr)
            return fr
        }
    }
}
