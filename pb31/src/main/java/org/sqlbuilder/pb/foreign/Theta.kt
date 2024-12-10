package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils
import java.util.Comparator
import java.util.Locale
import java.util.Objects

class Theta private constructor(thetaName: String) : HasId, Comparable<Theta>, Insertable {

    val theta: String = normalize(thetaName)

    @RequiresIdFrom(type = Theta::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val theta1 = o as Theta
        return theta == theta1.theta
    }

    override fun hashCode(): Int {
        return Objects.hash(theta)
    }

    // O R D E R

    override fun compareTo(that: Theta): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return String.format("'%s'", theta)
    }

    // T O S T R I N G

    override fun toString(): String {
        return theta
    }

    companion object {

        val COMPARATOR: Comparator<Theta> = Comparator.comparing<Theta, String> { it.theta }

        @JvmField
        val COLLECTOR = SetCollector<Theta>(COMPARATOR)

        @JvmStatic
        fun make(thetaName: String): Theta {
            val t = Theta(Utils.camelCase(thetaName))
            COLLECTOR.add(t)
            return t
        }

        @Suppress("unused")
        @RequiresIdFrom(type = Theta::class)
        fun getIntId(theta: Theta): Int {
            return COLLECTOR.invoke(theta)
        }

        private fun normalize(thetaName: String): String {
            return thetaName.substring(0, 1).uppercase(Locale.getDefault()) + thetaName.substring(1).lowercase(Locale.getDefault())
        }
    }
}