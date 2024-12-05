package org.sqlbuilder.pb.foreign

import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.pb.PbFnFrameResolvable
import org.sqlbuilder.pb.PbFnFrameResolved
import org.sqlbuilder.pb.objects.RoleSet
import org.sqlbuilder.pb.objects.Word

class RoleSetToFn private constructor(
    clazz: String,
    pos: String,
    pbRoleSet:
    RoleSet,
    word: Word,
) : RoleSetTo(clazz, pos, pbRoleSet, word), Resolvable<PbFnFrameResolvable, PbFnFrameResolved> {

    // R E S O L V E

    override fun resolving(): PbFnFrameResolvable {
        return ref
    }

    companion object {

        val COMPARATOR: Comparator<RoleSetToFn> = Comparator
            .comparing<RoleSetToFn, RoleSet> { it.pbRoleSet }
            .thenComparing { it.pbWord }
            .thenComparing { it.ref }
            .thenComparing { it.pos }

        val SET = HashSet<RoleSetToFn>()

        fun make(clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): RoleSetToFn {
            val a = RoleSetToFn(clazz, pos, pbRoleSet, word)
            SET.add(a)
            return a
        }
    }
}
