package org.sqlbuilder.vn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.vn.objects.VnClass
import org.sqlbuilder.vn.objects.Word
import java.util.*

class Class_Word private constructor(
	@JvmField val clazz: VnClass,
	@JvmField val word: Word
) : Insertable, Comparable<Class_Word> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Class_Word
        return clazz == that.clazz && word == that.word
    }

    override fun hashCode(): Int {
        return Objects.hash(clazz, word)
    }

    // O R D E R I N G

    override fun compareTo(that: Class_Word): Int {
        return COMPARATOR.compare(this, that)
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

        @JvmField
        val COMPARATOR: Comparator<Class_Word> = Comparator
            .comparing<Class_Word, Word> { it.word }
            .thenComparing<VnClass> { it.clazz }

        @JvmField
        val SET = HashSet<Class_Word>()

        @JvmStatic
        fun make(clazz: VnClass, word: Word): Class_Word {
            val m = Class_Word(clazz, word)
            SET.add(m)
            return m
        }
    }
}
