package org.sqlbuilder.pb.foreign

import org.sqlbuilder.pb.objects.RoleSet
import org.sqlbuilder.pb.objects.Word

class FnAlias private constructor(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word) : Alias(clazz, pos, pbRoleSet, word) {
    companion object {

        val COMPARATOR: Comparator<FnAlias> = Comparator
            .comparing<FnAlias, RoleSet> { it.pbRoleSet}
            .thenComparing {it.pbWord}
            .thenComparing {it.ref}
            .thenComparing {it.pos}

        val SET: MutableSet<FnAlias> = HashSet<FnAlias>()

        fun make(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): FnAlias {
            val a = FnAlias(clazz, pos, pbRoleSet, word)
            SET.add(a)
            return a
        }
    }
}
