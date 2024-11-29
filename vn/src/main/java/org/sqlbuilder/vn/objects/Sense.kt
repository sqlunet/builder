package org.sqlbuilder.vn.objects

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableQuotedString
import org.sqlbuilder.common.Utils.quote
import java.util.AbstractMap.SimpleEntry

class Sense private constructor(
    val sensekey: Sensekey,
) : Insertable, Resolvable<String, SimpleEntry<Int, Int>>, Comparable<Sense> {

    // O R D E R I N G

    override fun compareTo(that: Sense): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return quote(sensekey.toString())
    }

    // R E S O L V E

    override fun resolving(): String? {
        return sensekey.sensekey
    }

    // T O S T R I N G

    override fun toString(): String {
        return sensekey.toString()
    }

    companion object {

        val COMPARATOR: Comparator<Sense> = Comparator
            .comparing<Sense, Sensekey> { it.sensekey }

        @JvmField
        val SET = HashSet<Sense>()

        @JvmStatic
        fun make(sensekey: Sensekey): Sense {
            val m = Sense(sensekey)
            SET.add(m)
            return m
        }
    }
}
