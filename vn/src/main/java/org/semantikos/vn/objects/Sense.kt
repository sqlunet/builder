package org.semantikos.vn.objects

import org.semantikos.common.Insertable
import org.semantikos.common.Resolvable
import org.semantikos.common.Utils.quote
import org.semantikos.vn.VnSensekeyResolvable
import org.semantikos.vn.VnSensekeyResolved

class Sense private constructor(
    val sensekey: Sensekey,
) : Insertable, Resolvable<VnSensekeyResolvable, VnSensekeyResolved>, Comparable<Sense> {

    // O R D E R I N G

    override fun compareTo(that: Sense): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return quote(sensekey.toString())
    }

    // R E S O L V E

    override fun resolving(): VnSensekeyResolvable {
        return sensekey.sensekey
    }

    // T O S T R I N G

    override fun toString(): String {
        return sensekey.toString()
    }

    companion object {

        val COMPARATOR: Comparator<Sense> = Comparator
            .comparing<Sense, Sensekey> { it.sensekey }

        val SET = HashSet<Sense>()

        fun make(sensekey: Sensekey): Sense {
            val m = Sense(sensekey)
            SET.add(m)
            return m
        }
    }
}
