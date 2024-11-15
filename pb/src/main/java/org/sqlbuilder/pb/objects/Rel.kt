package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.NotNull
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils
import org.sqlbuilder.pb.PbNormalizer
import java.util.Comparator
import java.util.function.Function

class Rel private constructor(val example: Example, text: String, val f: Func?) : HasId, Insertable, Comparable<Rel?> {

    val text: String = PbNormalizer.normalize(text)

    @RequiresIdFrom(type = Rel::class)
    override fun getIntId(): Int {
        return COLLECTOR.get(this)!!
    }

    // O R D E R

    override fun compareTo(@NotNull that: Rel?): Int {
        return COMPARATOR.compare(this, that)
    }

    override fun dataRow(): String {
        // (relid),rel,func,exampleid
        return String.format(
            "'%s',%s,%s",
            Utils.escape(text),
            Utils.nullable<Func?>(f, Function { obj: Func? -> obj!!.getSqlId() }),
            example.getSqlId()
        )
    }

    override fun toString(): String {
        return String.format("rel %s[%s][%s]", text, example, f)
    }

    companion object {

        private val COMPARATOR: Comparator<Rel?> = Comparator
            .comparing<Rel?, Example?>(Function { obj: Rel? -> obj!!.example })
            .thenComparing<String?>(Function { obj: Rel? -> obj!!.text })
            .thenComparing<Func?>(Function { obj: Rel? -> obj!!.f }, Comparator.nullsFirst<Func?>(Comparator.naturalOrder<Func?>()))

        @JvmField
        val COLLECTOR: SetCollector<Rel?> = SetCollector<Rel?>(COMPARATOR)

        fun make(example: Example, text: String, f: Func?): Rel {
            val r = Rel(example, text, f)
            COLLECTOR.add(r)
            return r
        }
    }
}
