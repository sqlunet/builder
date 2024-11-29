package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import org.sqlbuilder.pb.PbNormalizer

class Arg private constructor(example: Example, text: String, val type: String) : HasId, Insertable, Comparable<Arg> {

    private val example: Example

    private val text: String

    private val n: ArgType

    private val f: Func?

    // C O N S T R U C T O R

    init {
        assert(!type.isEmpty())
        this.example = example
        this.text = PbNormalizer.normalize(text)
        val fn = extractFN(type)
        n = fn.first
        f = fn.second
    }

    private fun extractFN(type: String): Pair<ArgType, Func?> {
        val fields = type.split("-")
        val nFields = fields.size
        // find first field starting with 'ARG'
        var index = fields.indexOfFirst { it.startsWith("ARG") }
        // make
        val n = ArgType.make(fields[index].replace("ARG", ""))
        val f = if (nFields > index + 1) Func.makeOrNull(fields[index + 1]) else null
        return n to f
    }

    // N I D

    override fun getIntId(): Int {
        return COLLECTOR.apply(this)
    }

    // O R D E R

    override fun compareTo(that: Arg): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "arg $example[$n][$f]"
    }

    // I N S E R T

    @RequiresIdFrom(type = Func::class)
    @RequiresIdFrom(type = Example::class)
    override fun dataRow(): String {
        return "'${Utils.escape(text)}','${n.argType}',${if (f == null) "NULL" else Func.getIntId(f)},${example.intId}"
    }

    override fun comment(): String {
        return "${n.argType},${f?.func}"
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
