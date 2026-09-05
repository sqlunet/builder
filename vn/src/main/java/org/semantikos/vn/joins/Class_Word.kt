package org.semantikos.vn.joins

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.vn.objects.VnClass
import org.semantikos.vn.objects.Word
import java.util.*

class Class_Word private constructor(
	val clazz: VnClass,
	val word: Word
) : Insertable, Comparable<Class_Word> {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Class_Word
        return clazz == that.clazz && word == that.word
    }

    override fun hashCode(): Int {
        return Objects.hash(clazz, word)
    }

    // O R D E R I N G

    override fun compareTo(other: Class_Word): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    @RequiresIdFrom(type = VnClass::class)
    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
         return "${clazz.intId},${word.intId}"
    }

    override fun comment(): String {
        return "${clazz.name},${word.word}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$clazz-$word"
    }

    companion object {

        val COMPARATOR: Comparator<Class_Word> = compareBy<Class_Word> { it.word }
            .thenBy { it.clazz }

        val SET = HashSet<Class_Word>()

        fun make(clazz: VnClass, word: Word): Class_Word {
            val m = Class_Word(clazz, word)
            SET.add(m)
            return m
        }
    }
}
