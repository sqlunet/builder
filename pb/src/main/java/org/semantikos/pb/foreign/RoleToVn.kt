package org.semantikos.pb.foreign

import org.semantikos.common.Resolvable
import org.semantikos.pb.PbVnRoleResolvable
import org.semantikos.pb.PbVnRoleResolved
import org.semantikos.pb.objects.Role

class RoleToVn private constructor(
    role: Role,
    aliasRole: AliasRole,
) : RoleTo(role, aliasRole), Resolvable<PbVnRoleResolvable, PbVnRoleResolved> {

    // R E S O L V E

    override fun resolving(): PbVnRoleResolvable {
        return aliasRole.aliasClass.classTag to aliasRole.aliasLink
    }

    companion object {

        val COMPARATOR: Comparator<RoleToVn> = Comparator
            .comparing<RoleToVn, Role> { it.role }
            .thenComparing<AliasRole> { it.aliasRole }

        val SET: MutableSet<RoleToVn> = HashSet<RoleToVn>()

        val RESOLVE_RESULT_STRINGIFIER = { r: PbVnRoleResolved? -> if (r == null) "NULL,NULL,NULL" else "${r.first},${r.second},${r.third}" }

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
