package org.semantikos.pb.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.*
import org.semantikos.pb.PbWordResolvable
import org.semantikos.pb.PbWordResolved
import java.io.Serializable
import java.util.*

class Word private constructor(val word: String) : HasId, Insertable, Resolvable<PbWordResolvable, PbWordResolved>, Comparable<Word>, Serializable {

    // N I D

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

    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        return "'${Utils.escape(word)}'"
    }

    // R E S O L V E

    override fun resolving(): PbWordResolvable {
        return word
    }

    // T O S T R I N G

    override fun toString(): String {
        return word
    }

    companion object {

        val COMPARATOR: Comparator<Word> = Comparator
            .comparing<Word, String> { it.word }

        val COLLECTOR = SetCollector<Word>(COMPARATOR)

        fun make(word: String): Word {
            val w = Word(word)
            COLLECTOR.add(w)
            return w
        }
    }
}
