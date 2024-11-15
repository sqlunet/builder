package org.sqlbuilder.pb.foreign

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.pb.objects.RoleSet
import org.sqlbuilder.pb.objects.Word

open class VnAlias protected constructor(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word) : Alias(clazz, pos, pbRoleSet, word), Insertable {
    companion object {

        val COMPARATOR: Comparator<VnAlias> = Comparator
            .comparing<VnAlias, RoleSet> { it.pbRoleSet }
            .thenComparing { it.pbWord }
            .thenComparing { it.ref }
            .thenComparing { it.pos }

        val SET: MutableSet<VnAlias> = HashSet<VnAlias>()

        fun make(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): VnAlias {
            val a = VnAlias(clazz, pos, pbRoleSet, word)
            SET.add(a)
            return a
        }
    }
}
