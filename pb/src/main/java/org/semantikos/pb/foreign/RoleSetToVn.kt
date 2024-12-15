package org.semantikos.pb.foreign

import org.semantikos.common.Resolvable
import org.semantikos.pb.PbVnClassResolvable
import org.semantikos.pb.PbVnClassResolved
import org.semantikos.pb.foreign.AliasClass.Companion.toTag
import org.semantikos.pb.objects.RoleSet
import org.semantikos.pb.objects.Word

open class RoleSetToVn protected constructor(
    clazz: String,
    pos: String,
    pbRoleSet: RoleSet,
    word: Word,
) : RoleSetTo(clazz, pos, pbRoleSet, word), Resolvable<PbVnClassResolvable, PbVnClassResolved> {

    // R E S O L V E

    override fun resolving(): PbVnClassResolvable {
        return toTag(ref)
    }

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
