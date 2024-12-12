package org.sqlbuilder.sl.foreign

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.sl.PbVnRoleResolvable
import org.sqlbuilder.sl.PbVnRoleResolved
import org.sqlbuilder.sl.SlPbRoleResolvable
import org.sqlbuilder.sl.SlVnRoleResolvable
import java.util.*

class PbRole_VnRole private constructor(
    val pbRole: PbRole,
    val vnRole: VnRole,
) : Insertable, Resolvable<PbVnRoleResolvable, PbVnRoleResolved> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as PbRole_VnRole
        return pbRole == that.pbRole && vnRole == that.vnRole
    }

    override fun hashCode(): Int {
        return Objects.hash(pbRole, vnRole)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'${pbRole.roleSet}','${pbRole.arg}','${vnRole.vnClass}','${vnRole.theta.theta}'"
    }

    // R E S O L V E

    override fun resolving(): PbVnRoleResolvable {
        return PbVnRoleResolvable(SlPbRoleResolvable(pbRole.roleSet.lowercase(), pbRole.arg), SlVnRoleResolvable(vnRole.vnClass, vnRole.theta.theta))
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$pbRole - $vnRole"
    }

    companion object {

        val COMPARATOR: Comparator<PbRole_VnRole> = Comparator
            .comparing<PbRole_VnRole, PbRole> { it.pbRole }
            .thenComparing<VnRole> { it.vnRole }

        val SET: MutableSet<PbRole_VnRole> = TreeSet<PbRole_VnRole>(COMPARATOR)

        val RESOLVE_RESULT_STRINGIFIER = { r: PbVnRoleResolved? ->
            if (r == null)
                "NULL,NULL,NULL,NULL"
            else {
                val s1 = "${nullableInt(r.first.first)},${nullableInt(r.first.second)}"
                val s2 = "${nullableInt(r.second.first)},${nullableInt(r.second.second)},${nullableInt(r.second.third)}"
                "$s1,$s2"
            }
        }

        fun make(role: PbRole, vnRole: VnRole): PbRole_VnRole {
            val a = PbRole_VnRole(role, vnRole)
            SET.add(a)
            return a
        }
    }
}
