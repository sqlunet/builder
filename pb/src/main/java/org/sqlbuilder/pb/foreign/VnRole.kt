package org.sqlbuilder.pb.foreign

import org.sqlbuilder.common.NotNull
import org.sqlbuilder.pb.objects.Theta
import java.util.*
import java.util.function.Function

class VnRole private constructor(
// A C C E S S
    val vnClass: VnClass, val vnTheta: Theta
) : Comparable<VnRole?> {

    // I D E N T I T Y
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val vnRole = o as VnRole
        return vnClass.equals(vnRole.vnClass) && vnTheta.equals(vnRole.vnTheta)
    }

    override fun hashCode(): Int {
        return Objects.hash(vnClass, vnTheta)
    }

    // O R D E R I N G
    override fun compareTo(@NotNull that: VnRole?): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G
    override fun toString(): String {
        return String.format("%s[%s]", this.vnClass, this.vnTheta)
    }

    companion object {

        val COMPARATOR: Comparator<VnRole?> = Comparator.comparing<VnRole?, VnClass?>(Function { obj: VnRole? -> obj!!.vnClass }).thenComparing<Theta?>(Function { obj: VnRole? -> obj!!.vnTheta })

        // C O N S T R U C T O R
        fun make(vnClass: VnClass, vnTheta: Theta): VnRole {
            return VnRole(vnClass, vnTheta)
        }
    }
}
