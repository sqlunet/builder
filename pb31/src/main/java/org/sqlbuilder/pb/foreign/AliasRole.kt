package org.sqlbuilder.pb.foreign

import java.util.*

class AliasRole private constructor(
    val aliasClass: AliasClass,
    val aliasLink: Theta,
) : Comparable<AliasRole> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val aliasRole = o as AliasRole
        return aliasClass == aliasRole.aliasClass && aliasLink == aliasRole.aliasLink
    }

    override fun hashCode(): Int {
        return Objects.hash(aliasClass, aliasLink)
    }

    // O R D E R I N G

    override fun compareTo(that: AliasRole): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$aliasClass[$aliasLink]"
    }

    companion object {

        val COMPARATOR: Comparator<AliasRole> = Comparator
            .comparing<AliasRole, AliasClass> { it.aliasClass }
            .thenComparing { it.aliasLink }

        fun make(aliasVnClass: AliasClass, aliasLink: Theta): AliasRole {
            return AliasRole(aliasVnClass, aliasLink)
        }
    }
}
