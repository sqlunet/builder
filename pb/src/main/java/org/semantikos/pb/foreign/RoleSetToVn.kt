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
