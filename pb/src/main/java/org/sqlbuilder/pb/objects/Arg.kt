package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.NotNull
import org.sqlbuilder.common.Nullable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils
import org.sqlbuilder.pb.PbNormalizer
import java.util.Comparator
import java.util.Locale
import java.util.function.Function

class Arg private constructor(example: Example, text: String, n: String, f: String?) : HasId, Insertable, Comparable<Arg?> {

    private val example: Example

    private val text: String

    @NotNull
    private val n: ArgType?

    @Nullable
    private val f: Func?

    // C O N S T R U C T O R

    init {
        assert(n != null && !n.isEmpty())
        this.example = example
        this.text = PbNormalizer.normalize(text)
        this.n = ArgType.make(n)
        this.f = if (f == null || f.isEmpty()) null else Func.make(f.lowercase(Locale.getDefault()))
    }

    // A C C E S S

    fun getExample(): Example {
        return this.example
    }

    fun getText(): String {
        return this.text
    }

    @Nullable
    fun getF(): Func? {
        return this.f
    }

    @NotNull
    fun getN(): ArgType? {
        return this.n
    }

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
            .comparing<Arg?, Example?>(Function { obj: Arg? -> obj!!.getExample() })
            .thenComparing<String?>(Function { obj: Arg? -> obj!!.getText() })
            .thenComparing<ArgType?>(Function { obj: Arg? -> obj!!.getN() })
            .thenComparing<Func?>(Function { obj: Arg? -> obj!!.getF() }, Comparator.nullsFirst<Func?>(Comparator.naturalOrder<Func?>()))

        @JvmField
        val COLLECTOR: SetCollector<Arg?> = SetCollector<Arg?>(COMPARATOR)

        fun make(example: Example, text: String, @NotNull n: String?, f: String?): Arg {
            val a = Arg(example, text, n!!, f)
            COLLECTOR.add(a)
            return a
        }
    }
}
