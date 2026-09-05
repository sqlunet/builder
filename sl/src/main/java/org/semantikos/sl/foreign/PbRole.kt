package org.semantikos.sl.foreign

import java.util.*

class PbRole private constructor(
    val roleSet: String, val arg: String
) : Comparable<PbRole> {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as PbRole
        return roleSet == that.roleSet && arg == that.arg
    }

    override fun hashCode(): Int {
        return Objects.hash(roleSet, arg)
    }

    // O R D E R

    override fun compareTo(other: PbRole): Int {
        return COMPARATOR.compare(this, other)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "($roleSet,$arg)"
    }

    companion object {

        val COMPARATOR: Comparator<PbRole> = Comparator
            .comparing<PbRole, String>{ it.roleSet }
            .thenComparing<String> { it.arg }

        fun make(roleSet: String, arg: String): PbRole {
            return PbRole(roleSet, arg)
        }
    }
}
