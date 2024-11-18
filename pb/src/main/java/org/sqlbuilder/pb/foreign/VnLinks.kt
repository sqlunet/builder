package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils
import java.util.*

class VnLinks private constructor(links: Collection<String>) : HasId, Comparable<VnLinks>, Insertable {

    val names: Set<String> = normalize(links)

    // N I D

    @RequiresIdFrom(type = VnLinks::class)
    override fun getIntId(): Int {
        return COLLECTOR[this]!!
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val links = o as VnLinks
        return names == links.names
    }

    override fun hashCode(): Int {
        return Objects.hash(names)
    }

    // O R D E R

    override fun compareTo(that: VnLinks): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'${names.joinToString(separator = ",")}'"
    }

    // T O S T R I N G

    override fun toString(): String {
        return names.joinToString(separator = ",")
    }

    companion object {

        val COMPARATOR: Comparator<VnLinks> = Comparator.comparing<VnLinks, String> { it.names.toString() }

        @JvmField
        val COLLECTOR = SetCollector<VnLinks>(COMPARATOR)

        @JvmStatic
        fun make(links: Collection<String>): VnLinks {
            val t = VnLinks(normalize(links))
            COLLECTOR.add(t)
            return t
        }

        @Suppress("unused")
        @RequiresIdFrom(type = VnLinks::class)
        fun getIntId(links: VnLinks): Int {
            return COLLECTOR[links]!!
        }

        private fun normalize(link: String): String {
            return Utils.camelCase(link)
        }

        private fun normalize(links: Collection<String>): Set<String> {
            return links
                .asSequence()
                .map { normalize(it) }
                .toSet()
        }
    }
}