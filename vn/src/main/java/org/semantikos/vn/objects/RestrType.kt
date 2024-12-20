package org.semantikos.vn.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import java.util.*

class RestrType private constructor(
    val value: String,
    val type: String,
    val isSyntactic: Boolean,
) : HasId, Insertable, Comparable<RestrType> {

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
        val that = o as RestrType
        return isSyntactic == that.isSyntactic && value == that.value && type == that.type
    }

    override fun hashCode(): Int {
        return Objects.hash(value, type, isSyntactic)
    }

    // O R D E R I N G

    override fun compareTo(that: RestrType): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        val buffer = StringBuilder()
        buffer.append(value)
        buffer.append(type)
        if (isSyntactic) {
            buffer.append('*')
        }
        return buffer.toString()
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$value','$type',$isSyntactic"
    }

    companion object {

        val COMPARATOR: Comparator<RestrType> =
            Comparator.comparing<RestrType, String> { it.type }
                .thenComparing<String> { it.value }
                .thenComparing<Boolean> { it.isSyntactic }

        val COLLECTOR = SetCollector<RestrType>(COMPARATOR)

        fun make(value: String, type: String, isSyntactic: Boolean): RestrType {
            val r = RestrType(value, type, isSyntactic)
            COLLECTOR.add(r)
            return r
        }
    }
}
