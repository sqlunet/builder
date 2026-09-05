package org.semantikos.pm.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import java.util.*

class PmPredicate private constructor(
    val predicate: String
) : HasId, Insertable, Comparable<PmPredicate> {

    val word: String
        get() = predicate.substring(0, predicate.indexOf('.'))

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
        val that = other as PmPredicate
        return predicate == that.predicate
    }

    override fun hashCode(): Int {
        return Objects.hash(predicate)
    }

    // O R D E R

    override fun compareTo(other: PmPredicate): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$predicate'"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[PRED $predicate]"
    }

    companion object {

        val COMPARATOR: Comparator<PmPredicate> = Comparator
            .comparing { it.predicate }

        val COLLECTOR = SetCollector(COMPARATOR)

        fun make(predicate: String): PmPredicate {
            val p = PmPredicate(predicate)
            COLLECTOR.add(p)
            return p
        }
    }
}
