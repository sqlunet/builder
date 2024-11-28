package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils
import java.util.*

abstract class AliasRoleLinks internal constructor(
    links: Collection<String>,
) : HasId, Comparable<AliasRoleLinks>, Insertable {

    val names: Set<String> = normalize(links)

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val links = o as AliasRoleLinks
        return names == links.names
    }

    override fun hashCode(): Int {
        return Objects.hash(names)
    }

    // O R D E R

    override fun compareTo(that: AliasRoleLinks): Int {
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

        val COMPARATOR: Comparator<AliasRoleLinks> = Comparator.comparing<AliasRoleLinks, String> { it.names.toString() }

        protected fun normalize(link: String): String {
            return Utils.camelCase(link)
        }

        @JvmStatic
        protected fun normalize(links: Collection<String>): Set<String> {
            return links
                .asSequence()
                .map { normalize(it) }
                .toSet()
        }
    }
}