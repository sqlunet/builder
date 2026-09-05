package org.semantikos.sl.foreign

import org.semantikos.sl.objects.Theta
import java.util.*

class VnRole private constructor(
    val vnClass: String,
    val theta: Theta,
) : Comparable<VnRole> {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as VnRole
        return vnClass == that.vnClass && theta == that.theta
    }

    override fun hashCode(): Int {
        return Objects.hash(vnClass, theta)
    }

    // O R D E R

    override fun compareTo(other: VnRole): Int {
        return COMPARATOR.compare(this, other)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "($vnClass,$theta)"
    }

    companion object {

        val COMPARATOR: Comparator<VnRole> = compareBy<VnRole> { it.vnClass }
            .thenBy { it.theta }

        fun make(vnClass: String, theta: Theta): VnRole {
            return VnRole(vnClass, theta)
        }
    }
}
