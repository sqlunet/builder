package org.semantikos.pb31.objects

import org.semantikos.common.Insertable
import org.semantikos.common.Utils.nullableQuotedString
import java.util.*

class ArgType private constructor(n: String) : Comparable<ArgType>, Insertable {

    val argType: String = n.uppercase()

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as ArgType
        return argType == that.argType
    }

    override fun hashCode(): Int {
        return Objects.hash(argType)
    }

    // O R D E R

    override fun compareTo(that: ArgType): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$argType',${nullableQuotedString(PREDEFINED[argType])}"
    }

    companion object {

        val COMPARATOR: Comparator<ArgType> = Comparator.comparing<ArgType, String> { it.argType }

        val SET: MutableSet<ArgType> = HashSet<ArgType>()

        private val PREDEFINED = mapOf(
            "0" to "[0]",
            "1" to "[1]",
            "2" to "[2]",
            "3" to "[3]",
            "4" to "[4]",
            "5" to "[5]",
            "6" to "[6]",
            "M" to "modifier",
        )

        fun make(n: String): ArgType {
            val argType = ArgType(n.uppercase())
            SET.add(argType)
            return argType
        }
    }
}
