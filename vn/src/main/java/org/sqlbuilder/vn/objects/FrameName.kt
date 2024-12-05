package org.sqlbuilder.vn.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import java.util.*

class FrameName private constructor(
    name: String
) : HasId, Insertable, Comparable<FrameName> {

    val name: String = name.trim { it <= ' ' }.uppercase(Locale.getDefault()).replace("\\s+".toRegex(), " ")

    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (o !is FrameName) {
            return false
        }
        val that = o
        return name == that.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    // O R D E R I N G

    override fun compareTo(that: FrameName): Int {
        return COMPARATOR.compare(this, that)
    }

    override fun toString(): String {
        return name
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$name'"
    }

    companion object {

        val COMPARATOR: Comparator<FrameName> = Comparator.comparing<FrameName, String> { it.name }

        val COLLECTOR: SetCollector<FrameName> = SetCollector<FrameName>(COMPARATOR)

        // C O N S T R U C T O R
        fun make(name: String): FrameName {
            val n = FrameName(name)
            COLLECTOR.add(n)
            return n
        }
    }
}
