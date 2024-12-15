package org.semantikos.fn.objects

import edu.berkeley.icsi.framenet.FEGroupRealizationType
import edu.berkeley.icsi.framenet.FEValenceType
import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.ListCollector
import org.semantikos.common.SetId
import java.util.*
import java.util.stream.Collectors

class FEGroupRealization private constructor(
    fegr: FEGroupRealizationType,
    val luID: Int,
    val frameID: Int,
) : HasId, SetId, Insertable {

    private var id = 0

    val fENames: String = Arrays.stream<FEValenceType?>(fegr.getFEArray()).map<String?> { obj: FEValenceType? -> obj!!.getName() }.collect(Collectors.joining(","))

    private val total: Int = fegr.getTotal()

    @RequiresIdFrom(type = FEGroupRealization::class)
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
        val that = o as FEGroupRealization
        return fENames == that.fENames && luID == that.luID && frameID == that.frameID
    }

    override fun hashCode(): Int {
        return Objects.hash(fENames, luID, frameID)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "$intId,$total,$luID"
    }

    override fun comment(): String {
        return "fes={$fENames}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FEGR fes={$fENames} luid=$luID]"
    }

    companion object {

        val COMPARATOR: Comparator<FEGroupRealization> = Comparator
            .comparing<FEGroupRealization, Int> { it.luID }
            .thenComparing<String> { it.fENames }

        val LIST = ListCollector<FEGroupRealization>()

        fun make(fegr: FEGroupRealizationType, luid: Int, frameid: Int): FEGroupRealization {
            val r = FEGroupRealization(fegr, luid, frameid)
            LIST.add(r)
            return r
        }
    }
}
