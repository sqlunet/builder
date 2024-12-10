package org.sqlbuilder.pb.foreign

import org.sqlbuilder.pb.foreign.Theta
import java.util.*

class AliasRole private constructor(
    val vnClass: AliasClass, val vnTheta: Theta,
) : Comparable<AliasRole> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val vnRole = o as AliasRole
        return vnClass == vnRole.vnClass && vnTheta == vnRole.vnTheta
    }

    override fun hashCode(): Int {
        return Objects.hash(vnClass, vnTheta)
    }

    // O R D E R I N G

    override fun compareTo(that: AliasRole): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G
    override fun toString(): String {
        return String.format("%s[%s]", this.vnClass, this.vnTheta)
    }

    companion object {

        val COMPARATOR: Comparator<AliasRole> = Comparator
            .comparing<AliasRole, AliasClass> { it.vnClass }
            .thenComparing{ it.vnTheta }

        fun make(vnClass: AliasClass, vnTheta: Theta): AliasRole {
            return AliasRole(vnClass, vnTheta)
        }
    }
}
