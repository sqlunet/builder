package org.sqlbuilder.vn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.vn.objects.Grouping
import org.sqlbuilder.vn.objects.VnClass
import org.sqlbuilder.vn.objects.Word
import java.util.*

class Member_Grouping private constructor(
    val vnClass: VnClass,
    val word: Word,
    val grouping: Grouping,
) : Insertable, Comparable<Member_Grouping> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Member_Grouping
        return word == that.word && vnClass == that.vnClass && grouping == that.grouping
    }

    override fun hashCode(): Int {
        return Objects.hash(word, vnClass, grouping)
    }

    // O R D E R I N G

    override fun compareTo(that: Member_Grouping): Int {
        return COMPARATOR.compare(this, that)
    }

    override fun toString(): String {
        return "$word-$vnClass-$grouping"
    }

    // I N S E R T

    @RequiresIdFrom(type = VnClass::class)
    @RequiresIdFrom(type = Word::class)
    @RequiresIdFrom(type = Grouping::class)
    override fun dataRow(): String {
        return "${vnClass.intId},${word.intId},${grouping.intId}"
    }

    override fun comment(): String {
        return "${vnClass.name},${word.word},${grouping.name}"
    }

    companion object {

        val COMPARATOR: Comparator<Member_Grouping> = Comparator
            .comparing<Member_Grouping, Word> { it.word }
            .thenComparing<VnClass> { it.vnClass }
            .thenComparing<Grouping> { it.grouping }

        val SET: MutableSet<Member_Grouping> = HashSet<Member_Grouping>()

        fun make(clazz: VnClass, word: Word, grouping: Grouping): Member_Grouping {
            val m = Member_Grouping(clazz, word, grouping)
            SET.add(m)
            return m
        }
    }
}
