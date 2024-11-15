package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import org.sqlbuilder.pb.PbNormalizer

class Rel private constructor(val example: Example, text: String, val f: Func?) : HasId, Insertable, Comparable<Rel> {

    val text: String = PbNormalizer.normalize(text)

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
            "'%s',%s,%s",
            Utils.escape(text),
            Utils.nullable<Func?>(f) { it!!.sqlId },
            example.getSqlId()
        )
    }

    override fun toString(): String {
        return String.format("rel %s[%s][%s]", text, example, f)
    }

    companion object {

        private val COMPARATOR: Comparator<Rel> = Comparator
            .comparing<Rel, Example> { it.example }
            .thenComparing<String> { it.text }
            .thenComparing<Func?>( { it.f }, Comparator.nullsFirst<Func?>(Comparator.naturalOrder()))

        @JvmField
        val COLLECTOR = SetCollector<Rel>(COMPARATOR)

        fun make(example: Example, text: String, f: Func?): Rel {
            val r = Rel(example, text, f)
            COLLECTOR.add(r)
            return r
        }
    }
}
