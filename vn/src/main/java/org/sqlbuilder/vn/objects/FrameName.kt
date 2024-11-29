package org.sqlbuilder.vn.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.NotNull
import org.sqlbuilder.common.SetCollector
import java.util.*

class FrameName private constructor(
    name: String
) : HasId, Insertable, Comparable<FrameName> {

    val name: String = name.trim { it <= ' ' }.uppercase(Locale.getDefault()).replace("\\s+".toRegex(), " ")

    override fun getIntId(): Int {
        return COLLECTOR.apply(this)
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (o !is FrameName) {
            return false
        }
        val that = o
        return this.name == that.name
    }

    override fun hashCode(): Int {
        return this.name.hashCode()
    }

    // O R D E R I N G

    override fun compareTo(@NotNull that: FrameName): Int {
        return COMPARATOR.compare(this, that)
    }

    override fun toString(): String {
        return this.name
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$name'"
    }

    companion object {

        val COMPARATOR: Comparator<FrameName> = Comparator.comparing<FrameName, String> { it.name }

        @JvmField
        val COLLECTOR: SetCollector<FrameName> = SetCollector<FrameName>(COMPARATOR)

        // C O N S T R U C T O R
        @JvmStatic
        fun make(name: String): FrameName {
            val n = FrameName(name)
            COLLECTOR.add(n)
            return n
        }
    }
}
