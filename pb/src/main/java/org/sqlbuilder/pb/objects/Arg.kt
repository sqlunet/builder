package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import org.sqlbuilder.pb.PbNormalizer

class Arg private constructor(example: Example, text: String, type: String) : HasId, Insertable, Comparable<Arg> {

    private val example: Example

    private val text: String

    private val n: ArgType

    private val f: Func?

    // C O N S T R U C T O R

    init {
        assert(!type.isEmpty())
        this.example = example
        this.text = PbNormalizer.normalize(text)
        val fields = type.split("-")
        this.n = ArgType.make(fields[0])
        this.f = if (fields.size > 1) Func.make(fields[1].lowercase()) else null
    }

    // A C C E S S

    override fun getIntId(): Int {
        return COLLECTOR[this]!!
    }

    // O R D E R

    override fun compareTo(@NotNull that: Arg): Int {
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
            n.argType,
            if (f == null) "NULL" else Func.getIntId(f),
            example.intId
        )
    }

    override fun comment(): String {
        return String.format("%s,%s", n.argType, f?.func)
    }

    companion object {

        private val COMPARATOR: Comparator<Arg> = Comparator
            .comparing<Arg, Example> { it.example }
            .thenComparing<String> { it.text }
            .thenComparing<ArgType> { it.n }
            .thenComparing<Func>({ it.f }, Comparator.nullsFirst<Func>(Comparator.naturalOrder()))

        @JvmField
        val COLLECTOR = SetCollector<Arg>(COMPARATOR)

        fun make(example: Example, text: String, type: String): Arg {
            val a = Arg(example, text, type)
            COLLECTOR.add(a)
            return a
        }
    }
}
