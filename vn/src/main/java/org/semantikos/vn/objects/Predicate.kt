package org.semantikos.vn.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import java.util.*

class Predicate private constructor(
	val name: String
) : HasId, Insertable, Comparable<Predicate> {

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
        val that = other as Predicate
        return name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    // O R D E R I N G

    override fun compareTo(other: Predicate): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$name'"
    }

    companion object {

        val COMPARATOR: Comparator<Predicate> = Comparator.comparing<Predicate, String> { it.name }

        val COLLECTOR: SetCollector<Predicate> = SetCollector<Predicate>(COMPARATOR)

         fun make(name: String): Predicate {
            val p = Predicate(name)
            COLLECTOR.add(p)
            return p
        }
    }
}
