package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import java.io.Serializable
import java.util.*

class Role private constructor(
    val roleSet: RoleSet,
    val argType: String,
    func: String?,
    descriptor: String?,
    theta: String?,
) : HasId, Insertable, Comparable<Role>, Serializable {

    private val func: Func? = if (func == null || func.isEmpty()) null else Func.make(func)

    private val descr: String? = descriptor

    // role name for VerbNet
    val theta: Theta? = if (theta == null || theta.isEmpty()) null else Theta.make(theta)

    @RequiresIdFrom(type = Role::class)
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
        val that = o as Role
        return roleSet == that.roleSet && argType == that.argType && func == that.func
    }

    override fun hashCode(): Int {
        return Objects.hash(roleSet, argType, func)
    }

    // O R D E R I N G

    override fun compareTo(that: Role): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    @RequiresIdFrom(type = Func::class)
    @RequiresIdFrom(type = Theta::class)
    override fun dataRow(): String {
        // (roleid),argtype,theta,func,roledescr,rolesetid
        return String.format(
            "'%s',%s,%s,%s,%d",
            argType,
            Utils.nullable<Theta?>(theta) { it!!.sqlId },
            Utils.nullable<Func?>(func) { it!!.sqlId },
            Utils.nullableQuotedEscapedString(descr),
            roleSet.intId
        )
    }

    override fun comment(): String {
        return String.format("%s,%s,%s", roleSet.name, theta?.theta ?: "∅", func?.func ?: "∅")
    }

    // T O S T R I N G

    override fun toString(): String {
        if (this.descr == null) {
            return String.format("%s[%s-%s]", roleSet, argType, func)
        }
        return String.format("%s[%s-%s '%s']", roleSet, argType, func, descr)
    }

    companion object {

        val COMPARATOR: Comparator<Role> = Comparator
            .comparing<Role, RoleSet> { it.roleSet }
            .thenComparing<String> { it.argType }
            .thenComparing<Func?>({ it.func }, Comparator.nullsFirst<Func?>(Comparator.naturalOrder<Func?>()))

        @JvmField
        val COLLECTOR = SetCollector<Role>(COMPARATOR)

        fun make(roleSet: RoleSet, n: String, f: String?, descriptor: String?, theta: String?): Role {
            val r = Role(roleSet, n, f, descriptor, theta)
            COLLECTOR.add(r)
            return r
        }

        @Suppress("unused")
        @RequiresIdFrom(type = Role::class)
        fun getIntId(role: Role?): Int? {
            return if (role == null) null else COLLECTOR.invoke(role)
        }
    }
}
