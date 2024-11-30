package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.SemTypeType.SuperType
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.SemType

class SemType_SemTypeSuper private constructor(
    semtypeid: Int,
    supersemtypeid: Int
) : Pair<Int, Int>(semtypeid, supersemtypeid), Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$first,$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[superSEM semtypeid=$first supersemtypeid=$second]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<SemType_SemTypeSuper> = Comparator
            .comparing<SemType_SemTypeSuper, Int> { it.first }
            .thenComparing<Int> { it.second }

        @JvmField
        val SET = HashSet<SemType_SemTypeSuper>()

        @JvmStatic
        fun make(semtype: SemType, supersemtype: SuperType): SemType_SemTypeSuper {
            val tt = SemType_SemTypeSuper(semtype.iD, supersemtype.getSupID())
            SET.add(tt)
            return tt
        }
   }
}
