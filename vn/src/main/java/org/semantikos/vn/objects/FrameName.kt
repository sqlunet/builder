package org.semantikos.vn.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import java.util.*

class FrameName private constructor(
    name: String
) : HasId, Insertable, Comparable<FrameName> {

    val name: String = name.trim { it <= ' ' }.uppercase(Locale.getDefault()).replace("\\s+".toRegex(), " ")

    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        return other is FrameName && name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    // O R D E R I N G

    override fun compareTo(other: FrameName): Int {
        return COMPARATOR.compare(this, other)
    }

    override fun toString(): String {
        return name
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$name'"
    }

    companion object {

        val COMPARATOR: Comparator<FrameName> = Comparator.comparing { it.name }

        val COLLECTOR: SetCollector<FrameName> = SetCollector(COMPARATOR)

        // C O N S T R U C T O R
        fun make(name: String): FrameName {
            val n = FrameName(name)
            COLLECTOR.add(n)
            return n
        }
    }
}
