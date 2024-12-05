package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullable
import org.sqlbuilder.common.Utils.nullableQuotedEscapedString
import org.sqlbuilder.pb.foreign.RoleSetTo
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

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as RoleSet
        return predicate == that.predicate && name == that.name && roleSetTos == that.roleSetTos
    }

    override fun hashCode(): Int {
        return Objects.hash(predicate, name)
    }

    // O R D E R I N G

    override fun compareTo(that: RoleSet): Int {
        return COMPARATOR.compare(this, that)
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
            .thenComparing<String> { it.name }

        val COLLECTOR = SetCollector<RoleSet>(COMPARATOR)

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
