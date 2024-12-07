package org.sqlbuilder.vn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.vn.objects.Predicate
import org.sqlbuilder.vn.objects.Semantics
import java.util.*

class Predicate_Semantics private constructor(
    val predicate: Predicate,
    val semantics: Semantics,
) : Insertable, Comparable<Predicate_Semantics> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Predicate_Semantics
        return semantics == that.semantics && predicate == that.predicate
    }

    override fun hashCode(): Int {
        return Objects.hash(semantics, predicate)
    }

    override fun compareTo(that: Predicate_Semantics): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = Predicate::class)
    @RequiresIdFrom(type = Semantics::class)
    override fun dataRow(): String {
        return "${predicate.intId},${semantics.intId}"
    }

    override fun comment(): String {
        return predicate.name
    }

    companion object {

        val SET = HashSet<Predicate_Semantics>()

        val COMPARATOR: Comparator<Predicate_Semantics> =
            Comparator
                .comparing<Predicate_Semantics, Semantics> { it.semantics }
                .thenComparing<Predicate> { it.predicate }

        fun make(predicate: Predicate, semantics: Semantics): Predicate_Semantics {
            val m = Predicate_Semantics(predicate, semantics)
            SET.add(m)
            return m
        }
    }
}
