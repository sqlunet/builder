package org.semantikos.pb.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import java.util.*

class Member private constructor(val roleSet: RoleSet, val word: Word) : Insertable, Comparable<Member> {

    init {
        SET.add(this)
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Member
        return roleSet == that.roleSet && word == that.word
    }

    override fun hashCode(): Int {
        return Objects.hash(roleSet, word)
    }

    // O R D E R

    override fun compareTo(that: Member): Int {
        return COMPARATOR.compare(this, that)
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

        val COMPARATOR: Comparator<Member> = Comparator
            .comparing<Member, Word> { it.word }
            .thenComparing<RoleSet> { it.roleSet }

        val SET: MutableSet<Member> = HashSet<Member>()

        fun make(roleSet: RoleSet, word: Word): Member {
            val m = Member(roleSet, word)
            SET.add(m)
            return m
        }
    }
}
