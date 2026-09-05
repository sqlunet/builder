package org.semantikos.pm.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.ParseException
import org.semantikos.common.SetCollector
import java.util.*
import kotlin.Throws

class PmRole private constructor(
    val predicate: PmPredicate,
    val role: String,
    val pos: Char
) : HasId, Insertable, Comparable<PmRole> {

    @RequiresIdFrom(type = PmRole::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as PmRole
        return predicate == that.predicate && role == that.role && pos == that.pos
    }

    override fun hashCode(): Int {
        return Objects.hash(predicate, role, pos)
    }

    // O R D E R

    override fun compareTo(other: PmRole): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "${predicate.intId},'$role','$pos'"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "predicate=$predicate role=$role pos=$pos"
    }

    companion object {

        val COMPARATOR: Comparator<PmRole> = compareBy<PmRole> { it.predicate }
            .thenBy { it.role }
            .thenBy { it.pos }

        val COLLECTOR = SetCollector(COMPARATOR)

        @Throws(ParseException::class)
        fun parse(line: String): PmRole {
            // split into fields
            val columns = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (columns.size > PmEntry.SOURCE + 1) {
                throw ParseException("Line has more fields than expected")
            }
            return parse(columns)
        }

        fun parse(columns: Array<String>): PmRole {
            val predicate = columns[PmEntry.ID_PRED].substring(3)
            val role = columns[PmEntry.ID_ROLE].substring(3)
            val pos = columns[PmEntry.ID_POS].substring(3)
            return make(predicate, role, pos[0])
        }

        fun make(predicate: String, role: String, pos: Char): PmRole {
            val p = PmPredicate.make(predicate)
            val r = PmRole(p, role, pos)
            COLLECTOR.add(r)
            return r
        }
    }
}
