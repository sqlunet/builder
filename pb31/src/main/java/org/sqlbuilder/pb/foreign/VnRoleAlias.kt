package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.pb.objects.Role
import org.sqlbuilder.pb.objects.RoleSet
import java.util.*

class VnRoleAlias private constructor(
    val role: Role, val vnRole: VnRole,
) : Insertable, Resolvable<Pair<String, String>, Triple<Int, Int, Int>> {

    // I D E N T I T Y
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as VnRoleAlias
        return role == that.role && vnRole == that.vnRole
    }

    override fun hashCode(): Int {
        return Objects.hash(role, vnRole)
    }

    // I N S E R T
    @RequiresIdFrom(type = Role::class)
    @RequiresIdFrom(type = RoleSet::class)
    override fun dataRow(): String {
        // rolesetid,roleid,vnclassid,vnroleid,vnclass,vntheta
        return String.format(
            "%d,%d,'%s','%s'",
            role.roleSet.intId,
            role.intId,
            vnRole.vnClass.classTag,
            vnRole.vnTheta.theta
        )
    }

    override fun comment(): String {
        return String.format(
            "%s,%s,%s",
            role.roleSet.name,
            role.argType, role.theta
        )
    }

    // R E S O L V E

    override fun resolving(): Pair<String, String> {
        return vnRole.vnClass.classTag to  vnRole.vnTheta.theta
    }

    // T O S T R I N G

    override fun toString(): String {
        return String.format("%s > %s", role, vnRole)
    }

    companion object {

        val COMPARATOR: Comparator<VnRoleAlias> = Comparator
            .comparing<VnRoleAlias, Role> { obj: VnRoleAlias -> obj.role }
            .thenComparing<VnRole> { obj: VnRoleAlias -> obj.vnRole }

        val SET: MutableSet<VnRoleAlias> = HashSet<VnRoleAlias>()

        val RESOLVE_RESULT_STRINGIFIER = { r: Triple<Int, Int, Int>? -> if (r == null) "NULL,NULL,NULL" else String.format("%s,%s,%s", r.first, r.second, r.third) }

        fun make(role: Role, vnRole: VnRole): VnRoleAlias {
            val m = VnRoleAlias(role, vnRole)
            /* boolean wasThere = ! */
            SET.add(m)
            /*
			if (wasThere) {
				System.err.printf("%nduplicate %s%n", m);
			}
			*/
            return m
        }
    }
}
