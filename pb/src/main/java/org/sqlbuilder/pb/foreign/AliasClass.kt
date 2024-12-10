package org.sqlbuilder.pb.foreign

import java.util.*

class AliasClass private constructor(
    val head: String?,
    val classTag: String,
) : Comparable<AliasClass> {

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
        val that = o as AliasClass
        return head == that.head && classTag == that.classTag
    }

    override fun hashCode(): Int {
        return Objects.hash(head, classTag)
    }

    // O R D E R I N G

    override fun compareTo(that: AliasClass): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "<$classTag>"
    }

    companion object {

        val COMPARATOR: Comparator<AliasClass> = Comparator.comparing<AliasClass, String> { it.className }

        fun make(head: String?, classTag: String): AliasClass {
            return AliasClass(head, classTag)
        }

        fun toTag(className: String): String {
            return className.substring(className.indexOf('-') + 1)
        }
    }
}
