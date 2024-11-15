package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import org.sqlbuilder.pb.PbNormalizer
import java.util.*

class Arg private constructor(example: Example, text: String, n: String, f: String?) : HasId, Insertable, Comparable<Arg?> {

    private val example: Example

    private val text: String

    private val n: ArgType?

    private val f: Func?

    // C O N S T R U C T O R

    init {
        assert(!n.isEmpty())
        this.example = example
        this.text = PbNormalizer.normalize(text)
        this.n = ArgType.make(n)
        this.f = if (f == null || f.isEmpty()) null else Func.make(f.lowercase(Locale.getDefault()))
    }

    // A C C E S S

    override fun getIntId(): Int {
        return COLLECTOR[this]!!
    }

    // O R D E R

    override fun compareTo(@NotNull that: Arg?): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return String.format("arg %s[%s][%s]", example, n, f)
    }

    // I N S E R T

    @RequiresIdFrom(type = Func::class)
    @RequiresIdFrom(type = Example::class)
    override fun dataRow(): String {
        // (argid),text,n,f,exampleid
        return String.format(
            "'%s','%s',%s,%s",
            Utils.escape(text),
            n!!.argType,
            Func.getIntId(f),
            example.intId
        )
    }

    override fun comment(): String {
        return String.format("%s,%s", n!!.argType, f?.func)
    }

    companion object {

        private val COMPARATOR: Comparator<Arg?> = Comparator
            .comparing<Arg?, Example?> { it.example }
            .thenComparing<String?> { it.text }
            .thenComparing<ArgType?> { it.n }
            .thenComparing<Func?>({ it!!.f }, Comparator.nullsFirst<Func?>(Comparator.naturalOrder()))

        @JvmField
        val COLLECTOR: SetCollector<Arg?> = SetCollector<Arg?>(COMPARATOR)

        fun make(example: Example, text: String, @NotNull n: String?, f: String?): Arg {
            val a = Arg(example, text, n!!, f)
            COLLECTOR.add(a)
            return a
        }
    }
}
