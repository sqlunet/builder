package org.semantikos.fn.joins

import org.semantikos.common.Insertable
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

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as FE_SemType
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

        val COMPARATOR: Comparator<FE_SemType> = Comparator
            .comparing<FE_SemType, Int> { it.feid }
            .thenComparing { it.semtypeid }

        val SET = HashSet<FE_SemType>()

        fun make(feid: Int, semtypeid: Int): FE_SemType {
            val fs = FE_SemType(feid, semtypeid)
            SET.add(fs)
            return fs
        }
    }
}
