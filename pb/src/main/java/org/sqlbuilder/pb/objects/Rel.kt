package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import org.sqlbuilder.pb.PbNormalizer

class Rel private constructor(val example: Example, text: String) : HasId, Insertable, Comparable<Rel> {

    val text: String = PbNormalizer.normalize(text)

    // N I D

    @RequiresIdFrom(type = Rel::class)
    override fun getIntId(): Int {
        return COLLECTOR[this]!!
    }

    // O R D E R

    override fun compareTo(that: Rel): Int {
        return COMPARATOR.compare(this, that)
    }

    override fun dataRow(): String {
        // (relid),rel,func,exampleid
        return String.format(
            "'%s',%s",
            Utils.escape(text),
            example.getSqlId()
        )
    }

    override fun toString(): String {
        return String.format("rel %s[%s]", text, example)
    }

    companion object {

        private val COMPARATOR: Comparator<Rel> = Comparator
            .comparing<Rel, Example> { it.example }
            .thenComparing<String> { it.text }

        @JvmField
        val COLLECTOR = SetCollector<Rel>(COMPARATOR)

        fun make(example: Example, text: String): Rel {
            val r = Rel(example, text)
            COLLECTOR.add(r)
            return r
        }
    }
}
