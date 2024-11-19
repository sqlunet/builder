package org.sqlbuilder.pb.foreign

import java.util.*

class AliasRole private constructor(
    val aliasClass: AliasClass,
    val linksRoles: AliasRoleLinks,
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
        return aliasClass == aliasRole.aliasClass && this@AliasRole.linksRoles == aliasRole.linksRoles
    }

    override fun hashCode(): Int {
        return Objects.hash(aliasClass, linksRoles)
    }

    // O R D E R I N G

    override fun compareTo(that: AliasRole): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$aliasClass[$linksRoles]"
    }

    companion object {

        val COMPARATOR: Comparator<AliasRole> = Comparator
            .comparing<AliasRole, AliasClass> { it.aliasClass }
            .thenComparing { it.linksRoles }

        fun make(aliasVnClass: AliasClass, vnRoleLinks: AliasRoleLinks): AliasRole {
            return AliasRole(aliasVnClass, vnRoleLinks)
        }
    }
}
