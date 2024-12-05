package org.sqlbuilder.pb.foreign

import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.pb.objects.Role

class RoleToVn private constructor(
    role: Role,
    aliasRole: AliasRole,
) : RoleTo(role, aliasRole), Resolvable<Pair<String, String>, Triple<Int, Int, Int>> {

    // R E S O L V E

    override fun resolving(): Pair<String, String> {
        return aliasRole.aliasClass.classTag to aliasRole.aliasLink
    }

    companion object {

        val RESOLVE_RESULT_STRINGIFIER = { r: Triple<Int, Int, Int>? -> if (r == null) "NULL,NULL,NULL" else "${r.first},${r.second},${r.third}" }

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
				println("\nduplicate $m")
			}
			*/
            return m
        }
    }
}
