package org.semantikos.fn.joins

import edu.berkeley.icsi.framenet.SemTypeType.SuperType
import org.semantikos.common.Insertable
import org.semantikos.fn.objects.SemType
import java.util.*

data class SemType_SemTypeSuper(
    val semtypeid: Int,
    val supersemtypeid: Int,
) : Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$semtypeid,$supersemtypeid"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as SemType_SemTypeSuper
        return semtypeid == that.semtypeid && supersemtypeid == that.supersemtypeid
    }

    override fun hashCode(): Int {
        return Objects.hash(semtypeid, supersemtypeid)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[superSEM semtypeid=$semtypeid supersemtypeid=$supersemtypeid]"
    }

    companion object {

        val COMPARATOR: Comparator<SemType_SemTypeSuper> = Comparator
            .comparing<SemType_SemTypeSuper, Int> { it.semtypeid }
            .thenComparing<Int> { it.supersemtypeid }

        val SET = HashSet<SemType_SemTypeSuper>()

        fun make(semtype: SemType, supersemtype: SuperType): SemType_SemTypeSuper {
            val tt = SemType_SemTypeSuper(semtype.iD, supersemtype.getSupID())
            SET.add(tt)
            return tt
        }
    }
}
