package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.NotNull
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils
import org.sqlbuilder.pb.foreign.Alias
import java.io.Serializable
import java.util.ArrayList
import java.util.Comparator
import java.util.Objects
import java.util.function.Function

class RoleSet private constructor(private val predicate: Predicate, val name: String, private val descr: String?) : HasId, Insertable, Comparable<RoleSet?>, Serializable {

    internal val aliases: MutableList<Alias> = ArrayList<Alias>()

    @RequiresIdFrom(type = RoleSet::class)
    override fun getIntId(): Int {
        return COLLECTOR[this]!!
    }

    val head: String ?
        get() {
        return this.predicate.head
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
        return predicate == that.predicate && name == that.name && aliases == that.aliases
    }

    override fun hashCode(): Int {
        return Objects.hash(predicate, name)
    }

    // O R D E R I N G

    override fun compareTo(@NotNull that: RoleSet?): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        val predicate2 = predicate
        val word = LexItem.map[predicate2]

        // (rolesetid),rolesethead,rolesetname,rolesetdescr,pbwordid
        return String.format(
            "'%s',%s,'%s',%s",
            Utils.escape(name),
            Utils.nullableQuotedEscapedString<String?>(descr),
            Utils.escape(predicate.head),
            Utils.nullable<Word?>(word, Function { obj: Word? -> obj!!.getSqlId() })
        )
    }

    // T O S T R I N G

    override fun toString(): String {
        if (this.descr == null) {
            return String.format("<%s-%s>", head, this.name)
        }
        return String.format("<%s-%s-{%s}>", head, this.name, this.descr)
    }

    companion object {

        val COMPARATOR: Comparator<RoleSet> = Comparator
            .comparing<RoleSet, Predicate> { it.predicate }
            .thenComparing<String> { it.name }

        @JvmField
        val COLLECTOR: SetCollector<RoleSet> = SetCollector<RoleSet>(COMPARATOR)

        fun make(predicate: Predicate, roleSetId: String, name: String?): RoleSet {
            val s = RoleSet(predicate, roleSetId, name)
            COLLECTOR.add(s)
            return s
        }

        @Suppress("unused")
        @RequiresIdFrom(type = RoleSet::class)
        fun getIntId(roleset: RoleSet): Int {
            return COLLECTOR[roleset]!!
        }
    }
}
