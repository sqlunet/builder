package org.sqlbuilder.sl.foreign

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder2.ser.Pair
import org.sqlbuilder2.ser.Triplet
import java.util.*

class VnRoleAlias private constructor(
    val pbRole: PbRole,
    val vnRole: VnRole,
) : Insertable, Resolvable<Pair<Pair<String, String>, Pair<String, String>>?, Pair<Pair<Int, Int>, Triplet<Int, Int, Int>>> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as VnRoleAlias
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

    override fun resolving(): Pair<Pair<String, String>, Pair<String, String>> {
        return Pair(Pair(pbRole.roleSet, pbRole.arg), Pair(vnRole.vnClass, vnRole.theta.theta))
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$pbRole - $vnRole"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<VnRoleAlias> = Comparator
            .comparing<VnRoleAlias, PbRole> { it.pbRole }
            .thenComparing<VnRole> { it.vnRole }

        @JvmField
        val SET: MutableSet<VnRoleAlias> = TreeSet<VnRoleAlias>(COMPARATOR)

        @JvmField
        val RESOLVE_RESULT_STRINGIFIER: (Pair<Pair<Int, Int>, Triplet<Int, Int, Int>>?) -> String = {
            if (it == null)
                "NULL,NULL,NULL,NULL"
            else {
                val s1 = if (it.first == null) "NULL,NULL" else "${nullableInt(it.first.first)},${nullableInt(it.first.second)}"
                val s2 = if (it.second == null) "NULL,NULL,NULL" else "${nullableInt(it.second.first)},${nullableInt(it.second.second)},${nullableInt(it.second.third)}"
                "$s1,$s2"
            }
        }

        @JvmStatic
        fun make(role: PbRole, vnRole: VnRole): VnRoleAlias {
            val a = VnRoleAlias(role, vnRole)
            SET.add(a)
            return a
        }
    }
}
