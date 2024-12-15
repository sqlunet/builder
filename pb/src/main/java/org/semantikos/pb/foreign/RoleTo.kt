package org.semantikos.pb.foreign

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.pb.objects.Role
import org.semantikos.pb.objects.RoleSet
import java.util.*

abstract class RoleTo protected constructor(
    val role: Role,
    val aliasRole: AliasRole,
) : Insertable {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as RoleTo
        return role == that.role && aliasRole == that.aliasRole
    }

    override fun hashCode(): Int {
        return Objects.hash(role, aliasRole)
    }

    // I N S E R T

    @RequiresIdFrom(type = Role::class)
    @RequiresIdFrom(type = RoleSet::class)
    override fun dataRow(): String {
        return "${role.roleSet.intId}, ${role.intId},'${aliasRole.aliasClass.classTag}','${aliasRole.aliasLink}'"
    }

    override fun comment(): String {
        return "${role.roleSet.name},${role.argType},${role.aliasVnRoleLinks},${role.aliasFnFeLinks}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$role > $aliasRole"
    }
}
