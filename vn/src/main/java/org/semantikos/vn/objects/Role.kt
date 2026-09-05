package org.semantikos.vn.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils.nullableInt
import java.util.*

class Role private constructor(
    val clazz: VnClass,
    val restrRole: RestrainedRole,
) : Insertable, HasId {

    @Suppress("unused")
    val roleType: RoleType
        get() = restrRole.roleType

    @RequiresIdFrom(type = Role::class)
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
        val that = other as Role
        return restrRole == that.restrRole && clazz == that.clazz
    }

    override fun hashCode(): Int {
        return Objects.hash(restrRole, clazz)
    }

    // I N S E R T

    @RequiresIdFrom(type = VnClass::class)
    @RequiresIdFrom(type = RoleType::class)
    @RequiresIdFrom(type = Restrs::class)
    override fun dataRow(): String {
        val restrs = restrRole.restrs
        val restrsid = restrs?.intId
        return "${clazz.intId},${restrRole.roleType.intId},${nullableInt(restrsid)}"
    }

    override fun comment(): String {
        return "${clazz.name}, ${restrRole.roleType.type},${restrRole.restrs ?: "∅"}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "class=$clazz role=$restrRole"
    }

    companion object {

        val COMPARATOR: Comparator<Role> = Comparator
            .comparing<Role, VnClass> { it.clazz }
            .thenComparing { it.restrRole }

        val COLLECTOR: SetCollector<Role> = SetCollector(COMPARATOR)

        fun make(clazz: VnClass, restrainedRole: RestrainedRole): Role {
            val m = Role(clazz, restrainedRole)
            COLLECTOR.add(m)
            return m
        }
    }
}
