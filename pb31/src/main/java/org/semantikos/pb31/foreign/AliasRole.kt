package org.semantikos.pb31.foreign

import java.util.*

class AliasRole private constructor(
    val aliasClass: AliasClass,
    val aliasLink: Theta,
) : Comparable<AliasRole> {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val aliasRole = other as AliasRole
        return aliasClass == aliasRole.aliasClass && aliasLink == aliasRole.aliasLink
    }

    override fun hashCode(): Int {
        return Objects.hash(aliasClass, aliasLink)
    }

    // O R D E R I N G

    override fun compareTo(other: AliasRole): Int {
        return COMPARATOR.compare(this, other)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$aliasClass[$aliasLink]"
    }

    companion object {

        val COMPARATOR: Comparator<AliasRole> = compareBy<AliasRole> { it.aliasClass }
            .thenBy { it.aliasLink }

        fun make(aliasVnClass: AliasClass, aliasLink: Theta): AliasRole {
            return AliasRole(aliasVnClass, aliasLink)
        }
    }
}
