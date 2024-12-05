package org.sqlbuilder.fn.objects

import edu.berkeley.icsi.framenet.FERealizationType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.ListCollector
import org.sqlbuilder.common.SetId
import org.sqlbuilder.fn.types.FeType.getIntId
import java.util.*

class FERealization private constructor(
    fer: FERealizationType,
    val luId: Int,
    val frameId: Int,
) : HasId, SetId, Insertable {

    private var id = 0

    val fEName: String = fer.getFE().getName()

    private val total: Int = fer.getTotal()

    @RequiresIdFrom(type = FERealization::class)
    override fun getIntId(): Int {
        return id
    }

    override fun setId(id0: Int) {
        id = id0
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as FERealization
        return fEName == that.fEName && luId == that.luId && frameId == that.frameId
    }

    override fun hashCode(): Int {
        return Objects.hash(fEName, luId, frameId)
    }

    // I N S E R T

    override fun dataRow(): String {
        val fetypeid: Int = getIntId(fEName)!!
        val key = fetypeid to frameId
        val feid = FE.BY_FETYPEID_AND_FRAMEID!![key]!!.iD
        return "$intId,$fetypeid,$feid,$total,$luId"
    }

    override fun comment(): String {
        return "fe=$fEName"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FER fe=$fEName luid=$luId]"
    }

    companion object {

        val COMPARATOR: Comparator<FERealization> = Comparator
            .comparing<FERealization, Int> { it.luId }
            .thenComparing<String> { it.fEName }
            .thenComparing<Int> { it.frameId }

        val LIST = ListCollector<FERealization>()

        fun make(fer: FERealizationType, luid: Int, frameid: Int): FERealization {
            val r = FERealization(fer, luid, frameid)
            LIST.add(r)
            return r
        }
    }
}
