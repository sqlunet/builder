package org.sqlbuilder.pb31.foreign

import org.sqlbuilder.pb31.objects.RoleSet
import org.sqlbuilder.pb31.objects.Word

class RoleSetToFn private constructor(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word) : RoleSetTo(clazz, pos, pbRoleSet, word) {
    companion object {

        val COMPARATOR: Comparator<RoleSetToFn> = Comparator
            .comparing<RoleSetToFn, RoleSet> { it.pbRoleSet}
            .thenComparing {it.pbWord}
            .thenComparing {it.ref}
            .thenComparing {it.pos}

        val SET: MutableSet<RoleSetToFn> = HashSet<RoleSetToFn>()

        fun make(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): RoleSetToFn {
            val a = RoleSetToFn(clazz, pos, pbRoleSet, word)
            SET.add(a)
            return a
        }
    }
}
