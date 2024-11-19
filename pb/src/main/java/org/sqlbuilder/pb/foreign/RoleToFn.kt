package org.sqlbuilder.pb.foreign

import org.sqlbuilder.pb.objects.Role
import org.sqlbuilder2.ser.Triplet

class RoleToFn private constructor(
    role: Role,
    aliasRole: AliasRole,
) : RoleTo(role, aliasRole) {

    companion object {

        val RESOLVE_RESULT_STRINGIFIER = { r: Triplet<Int?, Int?, Int?>? -> if (r == null) "NULL,NULL,NULL" else "${r.first},${r.second};${r.third}" }

        val COMPARATOR: Comparator<RoleToFn> = Comparator
            .comparing<RoleToFn, Role> { it.role }
            .thenComparing<AliasRole> { it.aliasRole }

        val SET: MutableSet<RoleToFn> = HashSet<RoleToFn>()

        fun make(role: Role, aliasRole: AliasRole): RoleToFn {
            val m = RoleToFn(role, aliasRole)
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
