package org.sqlbuilder.pb.foreign

import java.util.*

class VnClass private constructor(
    val head: String?, val classTag: String,
) : Comparable<VnClass> {

    val className: String
        get() = "${head ?: "%"}-$classTag"

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as VnClass
        return head == that.head && classTag == that.classTag
    }

    override fun hashCode(): Int {
        return Objects.hash(head, classTag)
    }

    // O R D E R I N G

    override fun compareTo(that: VnClass): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "<$classTag>"
    }

    companion object {

        val COMPARATOR: Comparator<VnClass> = Comparator.comparing<VnClass, String> { it.className }

        fun make(head: String?, classTag: String): VnClass {
            return VnClass(head, classTag)
        }
    }
}
