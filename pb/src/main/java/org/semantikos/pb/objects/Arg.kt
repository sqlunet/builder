package org.semantikos.pb.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils.escape
import org.semantikos.pb.PbNormalizer

class Arg private constructor(example0: Example, text0: String, val type: String) : HasId, Insertable, Comparable<Arg> {

    private val example: Example = example0

    private val text: String = PbNormalizer.normalize(text0)

    private val n: ArgType

    private val f: Func?

    // C O N S T R U C T O R

    init {
        assert(!type.isEmpty())
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
        return COLLECTOR.invoke(this)
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
        return "'${escape(text)}','${n.argType}',${if (f == null) "NULL" else Func.getIntId(f)},${example.intId}"
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

        val COLLECTOR = SetCollector<Arg>(COMPARATOR)

        fun make(example: Example, text: String, type: String): Arg {
            val a = Arg(example, text, type)
            COLLECTOR.add(a)
            return a
        }
    }
}
