package org.semantikos.vn.joins

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.vn.objects.Predicate
import org.semantikos.vn.objects.Semantics
import java.util.*

class Predicate_Semantics private constructor(
    val predicate: Predicate,
    val semantics: Semantics,
) : Insertable, Comparable<Predicate_Semantics> {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Predicate_Semantics
        return semantics == that.semantics && predicate == that.predicate
    }

    override fun hashCode(): Int {
        return Objects.hash(semantics, predicate)
    }

    override fun compareTo(other: Predicate_Semantics): Int {
        return COMPARATOR.compare(this, other)
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
