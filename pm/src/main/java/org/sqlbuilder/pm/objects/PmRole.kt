package org.sqlbuilder.pm.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.ParseException
import org.sqlbuilder.common.SetCollector
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

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as PmRole
        return predicate == that.predicate && role == that.role && pos == that.pos
    }

    override fun hashCode(): Int {
        return Objects.hash(predicate, role, pos)
    }

    // O R D E R

    override fun compareTo(that: PmRole): Int {
        return COMPARATOR.compare(this, that)
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

        val COMPARATOR: Comparator<PmRole> = Comparator
            .comparing<PmRole, PmPredicate> { it.predicate }
            .thenComparing<String> { it.role }
            .thenComparing<Char> { it.pos }

        @JvmField
        val COLLECTOR = SetCollector<PmRole>(COMPARATOR)

        @JvmStatic
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
