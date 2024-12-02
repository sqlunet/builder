package org.sqlbuilder.sl.foreign

import java.util.*

class PbRole private constructor(
    val roleSet: String, val arg: String
) : Comparable<PbRole> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as PbRole
        return roleSet == that.roleSet && arg == that.arg
    }

    override fun hashCode(): Int {
        return Objects.hash(roleSet, arg)
    }

    // O R D E R

    override fun compareTo(that: PbRole): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "($roleSet,$arg)"
    }

    companion object {

        val COMPARATOR: Comparator<PbRole> = Comparator
            .comparing<PbRole, String>{ it.roleSet }
            .thenComparing<String> { it.arg }

        @JvmStatic
        fun make(roleSet: String, arg: String): PbRole {
            return PbRole(roleSet, arg)
        }
    }
}
