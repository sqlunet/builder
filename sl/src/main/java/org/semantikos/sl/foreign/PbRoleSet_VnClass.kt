package org.semantikos.sl.foreign

import org.semantikos.common.Insertable
import org.semantikos.common.Resolvable
import org.semantikos.sl.PbVnClassResolvable
import org.semantikos.sl.PbVnClassResolved
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

        val COMPARATOR: Comparator<PbRoleSet_VnClass> = Comparator
            .comparing<PbRoleSet_VnClass, String> { it.pbRoleset }
            .thenComparing<String> { it.vnClass }

        val SET: MutableSet<PbRoleSet_VnClass> = TreeSet<PbRoleSet_VnClass>(COMPARATOR)

        val RESOLVE_RESULT_STRINGIFIER  =  { r: PbVnClassResolved? ->
            if (r == null) "NULL,NULL" else "${r.first},${r.second}"
        }

        fun make(pbRoleSet: String, vnClass: String): PbRoleSet_VnClass {
            val a = PbRoleSet_VnClass(pbRoleSet, vnClass)
            SET.add(a)
            return a
        }
    }
}
