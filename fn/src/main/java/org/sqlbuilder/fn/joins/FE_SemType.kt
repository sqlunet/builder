package org.sqlbuilder.fn.joins

import org.sqlbuilder.common.Insertable
import java.util.*

data class FE_SemType(
    val feid: Int,
    val semtypeid: Int,
) : Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "$feid,$semtypeid"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as FE_SemType
        return feid == that.feid && semtypeid == that.semtypeid
    }

    override fun hashCode(): Int {
        return Objects.hash(feid, semtypeid)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FE-SEM feid=$feid semtypeid=$semtypeid]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<FE_SemType> = Comparator
            .comparing<FE_SemType, Int> { it.feid }
            .thenComparing<Int> { it.semtypeid }

        @JvmField
        val SET = HashSet<FE_SemType>()

        @JvmStatic
        fun make(feid: Int, semtypeid: Int): FE_SemType {
            val fs = FE_SemType(feid, semtypeid)
            SET.add(fs)
            return fs
        }
    }
}
