package org.sqlbuilder.pb.foreign

import org.sqlbuilder.pb.objects.Role

class RoleToFn private constructor(
    role: Role,
    aliasRole: AliasRole,
) : RoleTo(role, aliasRole) {

    companion object {

        val RESOLVE_RESULT_STRINGIFIER = { r: Triple<Int, Int, Int>? -> if (r == null) "NULL,NULL,NULL" else "${r.first},${r.second},${r.third}" }

        val COMPARATOR: Comparator<RoleToFn> = Comparator
            .comparing<RoleToFn, Role> { it.role }
            .thenComparing<AliasRole> { it.aliasRole }

        val SET: MutableSet<RoleToFn> = HashSet<RoleToFn>()

        fun make(role: Role, aliasRole: AliasRole): RoleToFn {
            val m = RoleToFn(role, aliasRole)
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
