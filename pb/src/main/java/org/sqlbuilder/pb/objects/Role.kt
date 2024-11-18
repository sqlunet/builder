package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import org.sqlbuilder.pb.foreign.FnLinks
import org.sqlbuilder.pb.foreign.VnLinks
import java.io.Serializable
import java.util.*

class Role private constructor(
    val roleSet: RoleSet,
    val argType: String,
    func: String,
    descriptor: String,
    vnLinks: Collection<String>?,
    fnLinks: Collection<String>?,
) : HasId, Insertable, Comparable<Role>, Serializable {

    private val func: Func = Func.make(func)

    private val descr: String = descriptor

    // role names for VerbNet and FrameNet
    val vnLinks: VnLinks? = if (vnLinks == null || vnLinks.isEmpty()) null else VnLinks.make(vnLinks)
    val fnLinks: FnLinks? = if (fnLinks == null || fnLinks.isEmpty()) null else FnLinks.make(fnLinks)

    // N I D

    @RequiresIdFrom(type = Role::class)
    override fun getIntId(): Int {
        return COLLECTOR.get(this)!!
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Role
        return roleSet == that.roleSet && argType == that.argType && func == that.func
    }

    override fun hashCode(): Int {
        return Objects.hash(roleSet, argType, func)
    }

    // O R D E R I N G

    override fun compareTo(@NotNull that: Role): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    @RequiresIdFrom(type = Func::class)
    @RequiresIdFrom(type = VnLinks::class)
    override fun dataRow(): String {
        return "'$argType',${Utils.nullable(vnLinks) { it!!.sqlId }},${func.sqlId},${Utils.quotedEscapedString(descr)},${roleSet.intId}"
    }

    override fun comment(): String {
        return "${roleSet.name}, ${vnLinks?.names ?: "∅"}, ${fnLinks?.names ?: "∅"}, $func"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$roleSet[$argType-$func '$descr']"
    }

    companion object {

        val COMPARATOR: Comparator<Role> = Comparator
            .comparing<Role, RoleSet> { it.roleSet }
            .thenComparing<String> { it.argType }
            .thenComparing<Func>({ it.func }, Comparator.nullsFirst<Func>(Comparator.naturalOrder<Func>()))

        @JvmField
        val COLLECTOR = SetCollector<Role>(COMPARATOR)

        fun make(roleSet: RoleSet, n: String, f: String, descriptor: String, vnLinks: Collection<String>?, fnLinks: Collection<String>?): Role {
            val r = Role(roleSet, n, f, descriptor, vnLinks, fnLinks)
            COLLECTOR.add(r)
            return r
        }

        @Suppress("unused")
        @RequiresIdFrom(type = Role::class)
        fun getIntId(role: Role): Int {
            return COLLECTOR[role]!!
        }
    }
}
