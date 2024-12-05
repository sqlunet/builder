package org.sqlbuilder.fn.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.fn.FnWordResolvable
import org.sqlbuilder.fn.FnWordResolved
import java.io.Serializable
import java.util.*

class Word private constructor(
    lemma: String,
) : HasId, Insertable, Resolvable<FnWordResolvable, FnWordResolved>, Comparable<Word>, Serializable {

    @JvmField
    val word: String = lemma.lowercase()

    @RequiresIdFrom(type = Word::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Word
        return word == that.word
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

    override fun resolving(): FnWordResolvable {
        return word
    }

    // T O S T R I N G

    override fun toString(): String {
        return word
    }

    companion object {

        val COMPARATOR: Comparator<Word> = Comparator
            .comparing<Word, String> { it.word }

        @JvmField
        val COLLECTOR = SetCollector<Word>(COMPARATOR)

        @JvmStatic
        fun make(lemma: String): Word {
            val w = Word(lemma)
            COLLECTOR.add(w)
            return w
        }
    }
}
