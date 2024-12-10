package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.pb.PbNormalizer

class Arg private constructor(example0: Example, text0: String, n0: String, f0: String?) : HasId, Insertable, Comparable<Arg> {

    private val example: Example = example0

    private val text: String = PbNormalizer.normalize(text0)

    private val n: ArgType

    private val f: Func?

    // C O N S T R U C T O R

    init {
        assert(!n0.isEmpty())
        n = ArgType.make(n0)
        f = if (f0 == null || f0.isEmpty()) null else Func.makeOrNull(f0.lowercase())
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

        fun make(example: Example, text: String, n: String, f: String?): Arg {
            val a = Arg(example, text, n, f)
            COLLECTOR.add(a)
            return a
        }
    }
}
