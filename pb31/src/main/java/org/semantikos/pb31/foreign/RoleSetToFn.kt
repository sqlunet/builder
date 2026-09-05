package org.semantikos.pb31.foreign

import org.semantikos.pb31.objects.RoleSet
import org.semantikos.pb31.objects.Word

class RoleSetToFn private constructor(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word) : RoleSetTo(clazz, pos, pbRoleSet, word) {
    companion object {

        val COMPARATOR: Comparator<RoleSetToFn> = compareBy<RoleSetToFn> { it.pbRoleSet}
            .thenBy {it.pbWord}
            .thenBy {it.ref}
            .thenBy {it.pos}

        val SET: MutableSet<RoleSetToFn> = HashSet()

        fun make(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): RoleSetToFn {
            val a = RoleSetToFn(clazz, pos, pbRoleSet, word)
            SET.add(a)
            return a
        }
    }
}
