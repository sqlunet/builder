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

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Predicate
        return name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    // O R D E R I N G

    override fun compareTo(that: Predicate): Int {
        return COMPARATOR.compare(this, that)
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
