package org.sqlbuilder.sl.foreign

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.sl.PbRoleResolvable
import org.sqlbuilder.sl.PbVnRoleResolvable
import org.sqlbuilder.sl.PbVnRoleResolved
import org.sqlbuilder.sl.VnRoleResolvable
import java.util.*
import java.util.function.Function

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
        return PbVnRoleResolvable(PbRoleResolvable(pbRole.roleSet, pbRole.arg), VnRoleResolvable(vnRole.vnClass, vnRole.theta.theta))
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$pbRole - $vnRole"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<PbRole_VnRole> = Comparator
            .comparing<PbRole_VnRole, PbRole> { it.pbRole }
            .thenComparing<VnRole> { it.vnRole }

        @JvmField
        val SET: MutableSet<PbRole_VnRole> = TreeSet<PbRole_VnRole>(COMPARATOR)

        @JvmField
        val RESOLVE_RESULT_STRINGIFIER = Function { r: PbVnRoleResolved? ->
            if (r == null)
                "NULL,NULL,NULL,NULL"
            else {
                val s1 = "${nullableInt(r.first.first)},${nullableInt(r.first.second)}"
                val s2 = "${nullableInt(r.second.first)},${nullableInt(r.second.second)},${nullableInt(r.second.third)}"
                "$s1,$s2"
            }
        }

        val RESOLVE_RESULT_STRINGIFIER0: (Pair<Pair<Int, Int>, Triple<Int, Int, Int>>?) -> String = {
            if (it == null)
                "NULL,NULL,NULL,NULL"
            else {
                val s1 = if (it.first == null) "NULL,NULL" else "${nullableInt(it.first.first)},${nullableInt(it.first.second)}"
                val s2 = if (it.second == null) "NULL,NULL,NULL" else "${nullableInt(it.second.first)},${nullableInt(it.second.second)},${nullableInt(it.second.third)}"
                "$s1,$s2"
            }
        }

        @JvmStatic
        fun make(role: PbRole, vnRole: VnRole): PbRole_VnRole {
            val a = PbRole_VnRole(role, vnRole)
            SET.add(a)
            return a
        }
    }
}
