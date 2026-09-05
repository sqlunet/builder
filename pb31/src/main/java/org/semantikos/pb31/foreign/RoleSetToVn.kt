package org.semantikos.pb31.foreign

import org.semantikos.common.Insertable
import org.semantikos.pb31.objects.RoleSet
import org.semantikos.pb31.objects.Word

open class RoleSetToVn protected constructor(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word) : RoleSetTo(clazz, pos, pbRoleSet, word), Insertable {
    companion object {

        val COMPARATOR: Comparator<RoleSetToVn> = compareBy<RoleSetToVn> { it.pbRoleSet }
            .thenBy { it.pbWord }
            .thenBy { it.ref }
            .thenBy { it.pos }

        val SET: MutableSet<RoleSetToVn> = HashSet()

        fun make(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): RoleSetToVn {
            val a = RoleSetToVn(clazz, pos, pbRoleSet, word)
            SET.add(a)
            return a
        }
    }
}
