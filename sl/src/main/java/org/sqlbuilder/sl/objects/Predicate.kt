package org.sqlbuilder.sl.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector

class Predicate private constructor(
    val predicate: String,
) : HasId, Comparable<Predicate>, Insertable {

    @RequiresIdFrom(type = Predicate::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // O R D E R

    override fun compareTo(that: Predicate): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$predicate'"
    }

    companion object {

        val COMPARATOR: Comparator<Predicate> = Comparator
            .comparing<Predicate, String> { it.predicate }

        val COLLECTOR = SetCollector<Predicate>(COMPARATOR)

        @RequiresIdFrom(type = Predicate::class)
        fun getIntId(predicate: Predicate?): Int? {
            return if (predicate == null) null else COLLECTOR.invoke(predicate)
        }

        fun make(lemma: String): Predicate {
            return Predicate(lemma)
        }
    }
}
