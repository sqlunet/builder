package org.sqlbuilder.vn.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.vn.VnWordResolvable
import org.sqlbuilder.vn.VnWordResolved
import java.io.Serializable
import java.util.*

class Word private constructor(
    @JvmField val word: String,
) : HasId, Insertable, Resolvable<VnWordResolvable, VnWordResolved>, Comparable<Word>, Serializable {

    @RequiresIdFrom(type = Word::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(that: Any?): Boolean {
        if (this === that) {
            return true
        }
        if (that == null || javaClass != that.javaClass) {
            return false
        }
        val vnWord = that as Word
        return word == vnWord.word
    }

    override fun hashCode(): Int {
        return Objects.hash(word)
    }

    // O R D E R

    override fun compareTo(that: Word): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'${escape(word)}'"
    }

    // R E S O L V E

    override fun resolving(): VnWordResolvable {
        return word
    }

    // T O S T R I N G

    override fun toString(): String {
        return word
    }

    companion object {

        val COMPARATOR: Comparator<Word> = Comparator.comparing<Word, String> { it.word }

        @JvmField
        val COLLECTOR: SetCollector<Word> = SetCollector<Word>(COMPARATOR)

        @JvmStatic
        fun make(word: String): Word {
            val w = Word(word)
            COLLECTOR.add(w)
            return w
        }
    }
}
