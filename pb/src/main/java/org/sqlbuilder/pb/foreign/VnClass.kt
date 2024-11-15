package org.sqlbuilder.pb.foreign

import org.sqlbuilder.common.NotNull
import java.util.*
import java.util.function.Function

class VnClass private constructor(
// A C C E S S
    val head: String?, val classTag: String
) : Comparable<VnClass?> {

    val className: String
        get() = String.format("%s-%s", head ?: "%", classTag)

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
    override fun compareTo(@NotNull that: VnClass?): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G
    override fun toString(): String {
        return String.format("<%s>", classTag)
    }

    companion object {

        val COMPARATOR: Comparator<VnClass?> = Comparator.comparing<VnClass?, String?>(Function { obj: VnClass? -> obj!!.className })

        // C O N S T R U C T O R
        fun make(head: String?, classTag: String): VnClass {
            return VnClass(head, classTag)
        }
    }
}
