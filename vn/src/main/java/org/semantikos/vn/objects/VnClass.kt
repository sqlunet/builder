package org.semantikos.vn.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import java.util.*

class VnClass private constructor(
	val name: String
) : HasId, Insertable, Comparable<VnClass> {

    val tag: String
        get() {
            val split = name.indexOf('-')
            return name.substring(split + 1)
        }

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
        val that = other as VnClass
        return name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    // O R D E R I N G

    override fun compareTo(other: VnClass): Int {
        return COMPARATOR.compare(this, other)
    }

    // T O S T R I N G

    override fun toString(): String {
        return name
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$name','$tag'"
    }

    companion object {

        val COMPARATOR: Comparator<VnClass> = compareBy { it.name }

        val COLLECTOR: SetCollector<VnClass> = SetCollector(COMPARATOR)

        fun make(name: String): VnClass {
            if (name.isEmpty()) {
                throw RuntimeException("No name")
            }

            val c = VnClass(name)
            COLLECTOR.add(c)
            return c
        }
    }
}
