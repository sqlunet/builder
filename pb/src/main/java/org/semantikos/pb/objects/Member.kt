package org.semantikos.pb.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import java.util.*

class Member private constructor(val roleSet: RoleSet, val word: Word) : Insertable, Comparable<Member> {

    init {
        SET.add(this)
    }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Member
        return roleSet == that.roleSet && word == that.word
    }

    override fun hashCode(): Int {
        return Objects.hash(roleSet, word)
    }

    // O R D E R

    override fun compareTo(other: Member): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        return "${roleSet.intId},${word.intId}"
    }

    override fun comment(): String {
        return "${roleSet.name},${word.word}"
    }

    companion object {

        val COMPARATOR: Comparator<Member> = compareBy<Member> { it.word }
            .thenBy { it.roleSet }

        val SET: MutableSet<Member> = HashSet<Member>()

        fun make(roleSet: RoleSet, word: Word): Member {
            val m = Member(roleSet, word)
            SET.add(m)
            return m
        }
    }
}
