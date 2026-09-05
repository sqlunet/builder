package org.semantikos.pb.foreign

import java.util.*

class AliasClass private constructor(
    val head: String?,
    val classTag: String,
) : Comparable<AliasClass> {

    val className: String
        get() = "${head ?: "%"}-$classTag"

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as AliasClass
        return head == that.head && classTag == that.classTag
    }

    override fun hashCode(): Int {
        return Objects.hash(head, classTag)
    }

    // O R D E R I N G

    override fun compareTo(other: AliasClass): Int {
        return COMPARATOR.compare(this, other)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "<$classTag>"
    }

    companion object {

        val COMPARATOR: Comparator<AliasClass> = compareBy { it.className }

        fun make(head: String?, classTag: String): AliasClass {
            return AliasClass(head, classTag)
        }

        fun toTag(className: String): String {
            return className.substring(className.indexOf('-') + 1)
        }
    }
}
