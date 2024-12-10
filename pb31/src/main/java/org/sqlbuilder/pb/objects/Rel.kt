package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullable
import org.sqlbuilder.pb.PbNormalizer

class Rel private constructor(val example: Example, text: String, val f: Func?) : HasId, Insertable, Comparable<Rel> {

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
        return "'${escape(text)}',${nullable(f) { it.sqlId }},${example.getSqlId()}"
    }

    override fun toString(): String {
        return "rel $text[$example]"
    }

    companion object {

        private val COMPARATOR: Comparator<Rel> = Comparator
            .comparing<Rel, Example> { it.example }
            .thenComparing<String> { it.text }
            .thenComparing<Func?>( { it.f }, Comparator.nullsFirst<Func?>(Comparator.naturalOrder()))

        val COLLECTOR = SetCollector<Rel>(COMPARATOR)

        fun make(example: Example, text: String, f: Func?): Rel {
            val r = Rel(example, text, f)
            COLLECTOR.add(r)
            return r
        }
    }
}
