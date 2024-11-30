package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.FEValenceType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.FE
import org.sqlbuilder.fn.objects.FEGroupRealization
import org.sqlbuilder.fn.types.FeType.getIntId

class FE_FEGroupRealization private constructor(
    feName: String,
    fegr: FEGroupRealization?,
) : Pair<String, FEGroupRealization>(feName, fegr), Insertable {

    val fEName: String
        get() = first

    val fENames: String
        get() = second!!.fENames

    // I N S E R T

    @RequiresIdFrom(type = FEGroupRealization::class)
    override fun dataRow(): String {
        val fetypeid: Int = getIntId(first)!!
        val key = Pair<Int, Int>(fetypeid, second!!.frameID)
        val feid = FE.BY_FETYPEID_AND_FRAMEID!![key]!!.iD
        return "${second.getSqlId()},$feid,$fetypeid"
    }

    override fun comment(): String {
        return "fe=$first,fes={$fENames},luid=${second.luID},frid=${second.frameID}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FE-FEGR fe=$first fegr={$second}]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<FE_FEGroupRealization> = Comparator
            .comparing<FE_FEGroupRealization, String> { it.fEName }
            .thenComparing<String> { it.fENames }

        @JvmField
        val SET = HashSet<FE_FEGroupRealization>()

        @JvmStatic
        fun make(fe: FEValenceType, fegr: FEGroupRealization): FE_FEGroupRealization {
            val fr = FE_FEGroupRealization(fe.getName(), fegr)
            SET.add(fr)
            return fr
        }
    }
}
