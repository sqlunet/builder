package org.sqlbuilder.vn.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.NotNull
import org.sqlbuilder.common.SetCollector
import java.util.*

class Grouping private constructor(
    @JvmField val name: String,
) : HasId, Insertable, Comparable<Grouping> {

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
        val that = o as Grouping
        return name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    // O R D E R I N G

    override fun compareTo(@NotNull that: Grouping): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return String.format("%s", this.name)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$name'"
    }

    companion object {

        val COMPARATOR: Comparator<Grouping> = Comparator.comparing<Grouping, String> { it.name }

        @JvmField
        val COLLECTOR: SetCollector<Grouping> = SetCollector<Grouping>(COMPARATOR)

        @JvmStatic
        fun make(groupingName: String): Grouping {
            val g = Grouping(groupingName)
            COLLECTOR.add(g)
            return g
        }
    }
}
