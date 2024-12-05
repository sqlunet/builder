package org.sqlbuilder.fn.objects

import edu.berkeley.icsi.framenet.GovernorType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.fn.objects.Word.Companion.make

class Governor private constructor(
    governor: GovernorType,
) : HasId, Insertable {

    val type: String = governor.getType()

    private val word: Word = make(governor.getLemma())

    fun getWord(): String {
        return word.word
    }

    @RequiresIdFrom(type = Governor::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I N S E R T

    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        return "'${escape(type)}',${word.getSqlId()}"
    }

    override fun comment(): String {
        return "word=${word.word}"
    }

    override fun toString(): String {
        return "[GOV type=$type word=${word}]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<Governor> = Comparator
            .comparing<Governor, String> { it.getWord() }
            .thenComparing<String> { it.type }

        @JvmField
        val COLLECTOR = SetCollector<Governor>(COMPARATOR)

        fun make(governor: GovernorType): Governor {
            val g = Governor(governor)
            COLLECTOR.add(g)
            return g
        }
    }
}