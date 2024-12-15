package org.semantikos.sl.foreign

import org.semantikos.sl.objects.Theta
import java.util.*

class VnRole private constructor(
    val vnClass: String,
    val theta: Theta,
) : Comparable<VnRole> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as VnRole
        return vnClass == that.vnClass && theta == that.theta
    }

    override fun hashCode(): Int {
        return Objects.hash(vnClass, theta)
    }

    // O R D E R

    override fun compareTo(that: VnRole): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "($vnClass,$theta)"
    }

    companion object {

        val COMPARATOR: Comparator<VnRole> = Comparator
            .comparing<VnRole, String> { it.vnClass }
            .thenComparing<Theta> { it.theta }

        fun make(vnClass: String, theta: Theta): VnRole {
            return VnRole(vnClass, theta)
        }
    }
}
