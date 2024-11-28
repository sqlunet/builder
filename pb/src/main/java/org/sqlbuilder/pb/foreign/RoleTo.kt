package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.pb.objects.Role
import org.sqlbuilder.pb.objects.RoleSet
import org.sqlbuilder2.ser.Pair
import org.sqlbuilder2.ser.Triplet
import java.util.*

abstract class RoleTo protected constructor(
    val role: Role,
    val aliasRole: AliasRole,
) : Insertable, Resolvable<Pair<String?, String?>, Triplet<Int?, Int?, Int?>?> {

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

    // R E S O L V E

    override fun resolving(): Pair<String?, String?> {
        return Pair<String?, String?>(aliasRole.aliasClass.classTag, aliasRole.aliasLink)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$role > $aliasRole"
    }
}
