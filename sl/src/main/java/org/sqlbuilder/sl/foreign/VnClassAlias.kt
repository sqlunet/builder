package org.sqlbuilder.sl.foreign

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder2.ser.Pair
import java.util.*

class VnClassAlias private constructor(
    val pbRoleset: String,
    val vnClass: String,
) : Insertable, Resolvable<Pair<String, String>, Pair<Int, Int>> {

    override fun dataRow(): String {
        return "'$pbRoleset','$vnClass'"
    }

    // R E S O L V E

    override fun resolving(): Pair<String, String> {
        return Pair<String, String>(pbRoleset, vnClass)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$pbRoleset - $vnClass"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<VnClassAlias> = Comparator
            .comparing<VnClassAlias, String> { it.pbRoleset }
            .thenComparing<String> { it.vnClass }

        @JvmField
        val SET: MutableSet<VnClassAlias> = TreeSet<VnClassAlias>(COMPARATOR)

        @JvmField
        val RESOLVE_RESULT_STRINGIFIER: (Pair<Int, Int>?) -> String = {
            if (it == null) "NULL,NULL" else "${it.first},${it.second}"
        }

        @JvmStatic
        fun make(pbRoleSet: String, vnClass: String): VnClassAlias {
            val a = VnClassAlias(pbRoleSet, vnClass)
            SET.add(a)
            return a
        }
    }
}
