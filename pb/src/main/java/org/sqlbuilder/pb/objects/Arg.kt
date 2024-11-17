package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import org.sqlbuilder.pb.PbNormalizer

class Arg private constructor(example: Example, text: String, val type: String) : HasId, Insertable, Comparable<Arg> {
    /*
    FOUND TYPES:
    ------------
    ARG0
    ARG1
    ARG2
    ARG3
    ARG4
    ARG5
    ARG6
    ARGA
    ARGM-ADJ
    ARGM-ADV
    ARGM-CAU
    ARGM-COM
    ARGM-CXN
    ARGM-DIR
    ARGM-DIS
    ARGM-DSP
    ARGM-EXT
    ARGM-GOL
    ARGM-LOC
    ARGM-LVB
    ARGM-MNR
    ARGM-MOD
    ARGM-NEG
    ARGM-PNC
    ARGM-PRD
    ARGM-PRP
    ARGM-PRR
    ARGM-REC
    ARGM-TMP
    ARGM-TOP

    C-ARG0
    C-ARG1
    C-ARG2
    C-ARG3
    C-ARG4
    C-ARGM-ADV
    C-ARGM-CAU
    C-ARGM-CXN
    C-ARGM-DSP
    C-ARGM-LOC
    C-ARGM-TMP

    R-ARG0
    R-ARG1
    R-ARG2
    R-ARG3
    R-ARG4
    R-ARGM-DIR
    R-ARGM-LOC
    R-ARGM-MNR
    R-ARGM-TMP
    R-C-ARG2
    keep pace
    */

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
        this.n = fn.first
        this.f = fn.second
    }

    private fun extractFN(type: String): Pair<ArgType, Func?> {
        val fields = this.type.split("-")
        val nFields = fields.size
        // find first field starting with 'ARG'
        var index = fields.indexOfFirst { it.startsWith("ARG") }
        // make
        val n = ArgType.make(fields[index].replace("ARG", ""))
        val f = if (nFields > index + 1) Func.make(fields[index + 1]) else null
        return n to f
    }

    // N I D

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
