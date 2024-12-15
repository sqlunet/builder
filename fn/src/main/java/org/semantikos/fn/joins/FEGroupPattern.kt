package org.semantikos.fn.joins

import edu.berkeley.icsi.framenet.FEGroupRealizationType
import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.ListCollector
import org.semantikos.common.SetId
import org.semantikos.fn.objects.FEGroupRealization
import java.util.*

class FEGroupPattern private constructor(
    pattern: FEGroupRealizationType.Pattern,
    val fegr: FEGroupRealization,
) : HasId, SetId, Insertable {

    private var id = 0

    val total: Int = pattern.getTotal()

    @RequiresIdFrom(type = FEGroupPattern::class)
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
        val that = o as FEGroupPattern
        return fegr == that.fegr
    }

    override fun hashCode(): Int {
        return Objects.hash(fegr)
    }

    // I N S E R T

    @RequiresIdFrom(type = FEGroupPattern::class)
    @RequiresIdFrom(type = FEGroupRealization::class)
    override fun dataRow(): String {
        return "$intId,$total,${fegr.getSqlId()}"
    }

    override fun comment(): String {
        return "fegr={${fegr.fENames}}"
    }

    override fun toString(): String {
        return "[GPAT fegr={${fegr.fENames}}]"
    }

    companion object {

        val COMPARATOR: Comparator<FEGroupPattern> = Comparator
            .comparing<FEGroupPattern, FEGroupRealization>({ it.fegr }, FEGroupRealization.COMPARATOR)

        val LIST: ListCollector<FEGroupPattern> = ListCollector<FEGroupPattern>()

        fun make(fegr: FEGroupRealization, pattern: FEGroupRealizationType.Pattern): FEGroupPattern {
            val p = FEGroupPattern(pattern, fegr)
            LIST.add(p)
            return p
        }
    }
}
