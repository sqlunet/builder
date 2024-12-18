package org.semantikos.vn.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import java.util.*

class RoleType private constructor(
    val type: String,
) : HasId, Insertable, Comparable<RoleType> {

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
        val that = o as RoleType
        return type == that.type
    }

    override fun hashCode(): Int {
        return Objects.hash(type)
    }

    // O R D E R I N G

    override fun compareTo(that: RoleType): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return type
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$type'"
    }

    companion object {

        val COMPARATOR: Comparator<RoleType> = Comparator.comparing<RoleType, String> { it.type }

        val COLLECTOR: SetCollector<RoleType> = SetCollector<RoleType>(COMPARATOR)

        // C O N S T R U C T O R
        fun make(type: String): RoleType {
            val t = RoleType(type)
            COLLECTOR.add(t)
            return t
        }
    }
}
