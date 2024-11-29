package org.sqlbuilder.vn.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import java.util.*

class VnClass private constructor(
	@JvmField val name: String
) : HasId, Insertable, Comparable<VnClass> {

    val tag: String
        get() {
            val split = name.indexOf('-')
            return name.substring(split + 1)
        }

    override fun getIntId(): Int {
        return COLLECTOR.apply(this)
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as VnClass
        return name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    // O R D E R I N G

    override fun compareTo(that: VnClass): Int {
        return COMPARATOR.compare(this, that)
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

        val COMPARATOR: Comparator<VnClass> = Comparator.comparing<VnClass, String> { it.name }

        @JvmField
        val COLLECTOR: SetCollector<VnClass> = SetCollector<VnClass>(COMPARATOR)

        @JvmStatic
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
