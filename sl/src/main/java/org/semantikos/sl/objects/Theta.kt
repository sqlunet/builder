package org.semantikos.sl.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils.camelCase
import java.util.*

class Theta private constructor(
    thetaName: String,
) : HasId, Comparable<Theta>, Insertable {

    val theta: String = normalize(thetaName)

    @RequiresIdFrom(type = Theta::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val theta1 = other as Theta
        return theta == theta1.theta
    }

    override fun hashCode(): Int {
        return Objects.hash(theta)
    }

    // O R D E R

    override fun compareTo(other: Theta): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$theta'"
    }

    companion object {

        val COMPARATOR: Comparator<Theta> = compareBy { it.theta }

        val COLLECTOR = SetCollector(COMPARATOR)

        @RequiresIdFrom(type = Theta::class)
        fun getIntId(theta: Theta?): Int? {
            return if (theta == null) null else COLLECTOR.invoke(theta)
        }

        private fun normalize(thetaName: String): String {
            return thetaName.substring(0, 1).uppercase(Locale.getDefault()) + thetaName.substring(1).lowercase(Locale.getDefault())
        }

        fun make(thetaName: String): Theta {
            val t = Theta(camelCase(thetaName))
            COLLECTOR.add(t)
            return t
        }
    }
}
