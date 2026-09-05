package org.semantikos.vn.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils.escape
import java.util.*

class FrameExample private constructor(
    val example: String
) : HasId, Insertable, Comparable<FrameExample> {

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
        val that = other as FrameExample
        return example == that.example
    }

    override fun hashCode(): Int {
        return Objects.hash(example)
    }

    // O R D E R I N G

    override fun compareTo(other: FrameExample): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'${escape(example)}'"
    }

    companion object {

        val COMPARATOR: Comparator<FrameExample> = compareBy { it.example }

        val COLLECTOR: SetCollector<FrameExample> = SetCollector(COMPARATOR)

         fun make(example: String): FrameExample {
            val e = FrameExample(example)
            COLLECTOR.add(e)
            return e
        }
    }
}
