package org.sqlbuilder.pm.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
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

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as PmPredicate
        return predicate == that.predicate
    }

    override fun hashCode(): Int {
        return Objects.hash(predicate)
    }

    // O R D E R

    override fun compareTo(that: PmPredicate): Int {
        return COMPARATOR.compare(this, that)
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
            .comparing<PmPredicate, String> { it.predicate }

        val COLLECTOR = SetCollector<PmPredicate>(COMPARATOR)

        fun make(predicate: String): PmPredicate {
            val p = PmPredicate(predicate)
            COLLECTOR.add(p)
            return p
        }
    }
}
