package org.sqlbuilder.vn.objects

import org.sqlbuilder.vn.objects.RoleType.Companion.make
import org.xml.sax.SAXException
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class RestrainedRole private constructor(
    @JvmField val roleType: RoleType, val restrs: Restrs?,
) : Comparable<RestrainedRole> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as RestrainedRole
        return roleType == that.roleType && restrs == that.restrs
    }

    override fun hashCode(): Int {
        return Objects.hash(roleType, restrs)
    }

    // O R D E R I N G

    override fun compareTo(that: RestrainedRole): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(roleType.type)
        if (restrs != null) {
            sb.append(' ')
            sb.append(restrs)
        }
        return sb.toString()
    }

    companion object {

        val COMPARATOR: Comparator<RestrainedRole> = Comparator
            .comparing<RestrainedRole, RoleType> { it.roleType }
            .thenComparing(
                { it.restrs }, Comparator.nullsFirst<Restrs?>(Comparator.naturalOrder())
            )

        val SET = HashSet<RestrainedRole>()

        @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
        fun make(type: String, restrsXML: String?): RestrainedRole {
            val roleType = make(type)
            val restrs = if (restrsXML == null || restrsXML.isEmpty() || restrsXML == "<SELRESTRS/>") null else Restrs.make(restrsXML, false)
            val r = RestrainedRole(roleType, restrs)
            SET.add(r)
            return r
        }
    }
}
