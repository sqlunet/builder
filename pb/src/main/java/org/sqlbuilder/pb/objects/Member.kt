package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.NotNull
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

    override fun compareTo(@NotNull that: Member): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        return String.format("%s,%s", roleSet.intId, word.intId)
    }

    override fun comment(): String {
        return String.format("%s,%s", roleSet.name, word.word)
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
