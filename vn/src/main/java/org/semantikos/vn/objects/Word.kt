package org.semantikos.vn.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.Resolvable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils.escape
import org.semantikos.vn.VnWordResolvable
import org.semantikos.vn.VnWordResolved
import java.io.Serializable
import java.util.*

class Word private constructor(
    val word: String,
) : HasId, Insertable, Resolvable<VnWordResolvable, VnWordResolved>, Comparable<Word>, Serializable {

    @RequiresIdFrom(type = Word::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val vnWord = other as Word
        return word == vnWord.word
    }

    override fun hashCode(): Int {
        return Objects.hash(word)
    }

    // O R D E R

    override fun compareTo(other: Word): Int {
        return COMPARATOR.compare(this, other)
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

        val COLLECTOR: SetCollector<Word> = SetCollector<Word>(COMPARATOR)

        fun make(word: String): Word {
            val w = Word(word)
            COLLECTOR.add(w)
            return w
        }
    }
}
