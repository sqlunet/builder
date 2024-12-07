package org.sqlbuilder.vn.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.nullableInt
import java.util.*

class Role private constructor(
    val clazz: VnClass,
    val restrRole: RestrainedRole
) : Insertable, HasId {

    @Suppress("unused")
    val roleType: RoleType
        get() = restrRole.roleType

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
        val r = restrRole.restrs
        return  "${clazz.name}, ${restrRole.roleType.type},${r}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "class=$clazz role=$restrRole"
    }

    companion object {

        val COMPARATOR: Comparator<Role> = Comparator
            .comparing<Role, VnClass> { it.clazz }
            .thenComparing<RestrainedRole> { it.restrRole }

        val COLLECTOR: SetCollector<Role> = SetCollector<Role>(COMPARATOR)

        fun make(clazz: VnClass, restrainedRole: RestrainedRole): Role {
            val m = Role(clazz, restrainedRole)
            COLLECTOR.add(m)
            return m
        }
    }
}
