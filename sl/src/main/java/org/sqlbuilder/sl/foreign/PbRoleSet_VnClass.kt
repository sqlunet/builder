package org.sqlbuilder.sl.foreign

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.sl.PbRoleSetResolvable
import org.sqlbuilder.sl.PbRoleSetResolved
import org.sqlbuilder.sl.PbVnClassResolvable
import org.sqlbuilder.sl.PbVnClassResolved
import org.sqlbuilder.sl.VnClassResolvable
import org.sqlbuilder.sl.VnClassResolved
import org.sqlbuilder2.ser.Pair
import java.util.*

class PbRoleSet_VnClass private constructor(
    val pbRoleset: String,
    val vnClass: String,
) : Insertable, Resolvable<PbVnClassResolvable, PbVnClassResolved> {

    override fun dataRow(): String {
        return "'$pbRoleset','$vnClass'"
    }

    // R E S O L V E

    override fun resolving(): PbVnClassResolvable {
        return PbVnClassResolvable(pbRoleset, vnClass)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$pbRoleset - $vnClass"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<PbRoleSet_VnClass> = Comparator
            .comparing<PbRoleSet_VnClass, String> { it.pbRoleset }
            .thenComparing<String> { it.vnClass }

        @JvmField
        val SET: MutableSet<PbRoleSet_VnClass> = TreeSet<PbRoleSet_VnClass>(COMPARATOR)

        @JvmField
        val RESOLVE_RESULT_STRINGIFIER: (Pair<Int, Int>?) -> String = {
            if (it == null) "NULL,NULL" else "${it.first},${it.second}"
        }

        @JvmStatic
        fun make(pbRoleSet: String, vnClass: String): PbRoleSet_VnClass {
            val a = PbRoleSet_VnClass(pbRoleSet, vnClass)
            SET.add(a)
            return a
        }
    }
}
