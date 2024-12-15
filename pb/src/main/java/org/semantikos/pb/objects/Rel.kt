package org.semantikos.pb.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils
import org.semantikos.pb.PbNormalizer

class Rel private constructor(val example: Example, text: String) : HasId, Insertable, Comparable<Rel> {

    val text: String = PbNormalizer.normalize(text)

    // N I D

    @RequiresIdFrom(type = Rel::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // O R D E R

    override fun compareTo(that: Rel): Int {
        return COMPARATOR.compare(this, that)
    }

    override fun dataRow(): String {
        return "'${Utils.escape(text)}',${example.getSqlId()}"
    }

    override fun toString(): String {
        return "rel $text[$example]"
    }

    companion object {

        private val COMPARATOR: Comparator<Rel> = Comparator
            .comparing<Rel, Example> { it.example }
            .thenComparing<String> { it.text }

        val COLLECTOR = SetCollector<Rel>(COMPARATOR)

        fun make(example: Example, text: String): Rel {
            val r = Rel(example, text)
            COLLECTOR.add(r)
            return r
        }
    }
}
