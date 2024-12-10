package org.sqlbuilder.pb31.foreign

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.pb31.objects.RoleSet
import org.sqlbuilder.pb31.objects.Word

open class RoleSetToVn protected constructor(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word) : RoleSetTo(clazz, pos, pbRoleSet, word), Insertable {
    companion object {

        val COMPARATOR: Comparator<RoleSetToVn> = Comparator
            .comparing<RoleSetToVn, RoleSet> { it.pbRoleSet }
            .thenComparing { it.pbWord }
            .thenComparing { it.ref }
            .thenComparing { it.pos }

        val SET: MutableSet<RoleSetToVn> = HashSet<RoleSetToVn>()

        fun make(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): RoleSetToVn {
            val a = RoleSetToVn(clazz, pos, pbRoleSet, word)
            SET.add(a)
            return a
        }
    }
}
