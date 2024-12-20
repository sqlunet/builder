package org.semantikos.pb31.foreign

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.common.Resolvable
import org.semantikos.common.Utils.nullable
import org.semantikos.pb31.objects.Role
import org.semantikos.pb31.objects.RoleSet
import java.util.*

class RoleToVn private constructor(
    val role: Role,
    val aliasRole: AliasRole,
) : Insertable, Resolvable<Pair<String, String>, Triple<Int, Int, Int>> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as RoleToVn
        return role == that.role && aliasRole == that.aliasRole
    }

    override fun hashCode(): Int {
        return Objects.hash(role, aliasRole)
    }

    // I N S E R T

    @RequiresIdFrom(type = Role::class)
    @RequiresIdFrom(type = RoleSet::class)
    override fun dataRow(): String {
        return "${role.roleSet.intId},${role.intId},'${aliasRole.aliasClass.classTag}','${aliasRole.aliasLink.theta}'"
    }

    override fun comment(): String {
        return "${role.roleSet.name},${role.argType},${nullable(role.vnLink)}"
    }

    // R E S O L V E

    override fun resolving(): Pair<String, String> {
        return aliasRole.aliasClass.classTag to aliasRole.aliasLink.theta
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$role > $aliasRole"
    }

    companion object {

        val COMPARATOR: Comparator<RoleToVn> = Comparator
            .comparing<RoleToVn, Role> { it.role }
            .thenComparing<AliasRole> { it.aliasRole }

        val SET: MutableSet<RoleToVn> = HashSet<RoleToVn>()

        val RESOLVE_RESULT_STRINGIFIER = { r: Triple<Int, Int, Int>? -> if (r == null) "NULL,NULL,NULL" else "${r.first},${r.second},${r.third}" }

        fun make(role: Role, aliasRole: AliasRole): RoleToVn {
            val m = RoleToVn(role, aliasRole)
            /* val wasThere = ! */
            SET.add(m)
            /*
			if (wasThere) {
				println("\nduplicate $m")
			}
			*/
            return m
        }
    }
}
