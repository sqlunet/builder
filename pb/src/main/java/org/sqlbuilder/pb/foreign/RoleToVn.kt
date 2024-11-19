package org.sqlbuilder.pb.foreign

import org.sqlbuilder.pb.objects.Role
import org.sqlbuilder2.ser.Triplet

class RoleToVn private constructor(
    role: Role,
    aliasRole: AliasRole,
) : RoleTo(role, aliasRole) {

    companion object {

        val RESOLVE_RESULT_STRINGIFIER = { r: Triplet<Int?, Int?, Int?>? -> if (r == null) "NULL,NULL,NULL" else "${r.first},${r.second},${r.third}" }

        val COMPARATOR: Comparator<RoleToVn> = Comparator
            .comparing<RoleToVn, Role> { it.role }
            .thenComparing<AliasRole> { it.aliasRole }

        val SET: MutableSet<RoleToVn> = HashSet<RoleToVn>()

        fun make(role: Role, aliasRole: AliasRole): RoleToVn {
            val m = RoleToVn(role, aliasRole)
            /* val wasThere = ! */
            SET.add(m)
            /*
			if (wasThere) {
				print("%nduplicate $m%n")
			}
			*/
            return m
        }
    }
}
