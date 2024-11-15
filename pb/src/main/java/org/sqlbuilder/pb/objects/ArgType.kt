package org.sqlbuilder.pb.objects

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.NotNull
import org.sqlbuilder.common.Utils
import java.util.Comparator
import java.util.HashSet
import java.util.Objects
import java.util.Properties
import java.util.function.Function

class ArgType private constructor(n: String) : Comparable<ArgType?>, Insertable {

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

    override fun compareTo(@NotNull that: ArgType?): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return String.format("'%s',%s", argType, Utils.nullableQuotedString<String?>(DESCRIPTIONS.getProperty(argType, null)))
    }

    companion object {

        val COMPARATOR: Comparator<ArgType?> = Comparator.comparing<ArgType?, String?>(Function { obj: ArgType? -> obj!!.argType })

        val SET: MutableSet<ArgType?> = HashSet<ArgType?>()

        private val DESCRIPTIONS = Properties()

        init {
            DESCRIPTIONS.setProperty("0", "[0]")
            DESCRIPTIONS.setProperty("1", "[1]")
            DESCRIPTIONS.setProperty("2", "[2]")
            DESCRIPTIONS.setProperty("3", "[3]")
            DESCRIPTIONS.setProperty("4", "[4]")
            DESCRIPTIONS.setProperty("5", "[5]")
            DESCRIPTIONS.setProperty("6", "[6]")
            DESCRIPTIONS.setProperty("M", "modifier")
            DESCRIPTIONS.setProperty("A", "agent")
            DESCRIPTIONS.setProperty("@", "?")
        }

        fun make(n: String?): ArgType? {
            if (n == null || n.isEmpty()) {
                return null
            }
            val a = ArgType(n)
            SET.add(a)
            return a
        }
    }
}
