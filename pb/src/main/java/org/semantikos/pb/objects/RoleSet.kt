package org.semantikos.pb.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils.escape
import org.semantikos.common.Utils.nullable
import org.semantikos.common.Utils.nullableQuotedEscapedString
import org.semantikos.pb.foreign.RoleSetTo
import java.io.Serializable
import java.util.*

class RoleSet private constructor(private val predicate: Predicate, val name: String, private val descr: String) : HasId, Insertable, Comparable<RoleSet>, Serializable {

    internal val roleSetTos: MutableList<RoleSetTo> = ArrayList<RoleSetTo>()

    val head: String
        get() {
            return predicate.head
        }

    // N I D

    @RequiresIdFrom(type = RoleSet::class)
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
        val that = other as RoleSet
        return predicate == that.predicate && name == that.name && roleSetTos == that.roleSetTos
    }

    override fun hashCode(): Int {
        return Objects.hash(predicate, name)
    }

    // O R D E R I N G

    override fun compareTo(other: RoleSet): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        val predicate2 = predicate
        val word = LexItem.map[predicate2]
        return "'${escape(name)}',${nullableQuotedEscapedString(descr)},'${escape(predicate.head)}',${nullable(word) { it.sqlId }}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "<$head-$name-{$descr}>"
    }

    companion object {

        val COMPARATOR: Comparator<RoleSet> = Comparator
            .comparing<RoleSet, Predicate> { it.predicate }
            .thenComparing { it.name }

        val COLLECTOR = SetCollector(COMPARATOR)

        fun make(predicate: Predicate, roleSetId: String, name: String): RoleSet {
            val s = RoleSet(predicate, roleSetId, name)
            COLLECTOR.add(s)
            return s
        }

        @Suppress("unused")
        @RequiresIdFrom(type = RoleSet::class)
        fun getIntId(roleset: RoleSet): Int {
            return COLLECTOR.invoke(roleset)
        }
    }
}
