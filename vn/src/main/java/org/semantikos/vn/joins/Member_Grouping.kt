package org.semantikos.vn.joins

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.vn.objects.Grouping
import org.semantikos.vn.objects.VnClass
import org.semantikos.vn.objects.Word
import java.util.*

class Member_Grouping private constructor(
    val vnClass: VnClass,
    val word: Word,
    val grouping: Grouping,
) : Insertable, Comparable<Member_Grouping> {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Member_Grouping
        return word == that.word && vnClass == that.vnClass && grouping == that.grouping
    }

    override fun hashCode(): Int {
        return Objects.hash(word, vnClass, grouping)
    }

    // O R D E R I N G

    override fun compareTo(other: Member_Grouping): Int {
        return COMPARATOR.compare(this, other)
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

        val COMPARATOR: Comparator<Member_Grouping> = compareBy<Member_Grouping> { it.word }
            .thenBy { it.vnClass }
            .thenBy { it.grouping }

        val SET: MutableSet<Member_Grouping> = HashSet<Member_Grouping>()

        fun make(clazz: VnClass, word: Word, grouping: Grouping): Member_Grouping {
            val m = Member_Grouping(clazz, word, grouping)
            SET.add(m)
            return m
        }
    }
}
