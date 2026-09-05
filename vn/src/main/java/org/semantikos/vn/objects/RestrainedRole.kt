package org.semantikos.vn.objects

import org.semantikos.vn.objects.RoleType.Companion.make
import org.xml.sax.SAXException
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class RestrainedRole private constructor(
    val roleType: RoleType, val restrs: Restrs?,
) : Comparable<RestrainedRole> {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as RestrainedRole
        return roleType == that.roleType && restrs == that.restrs
    }

    override fun hashCode(): Int {
        return Objects.hash(roleType, restrs)
    }

    // O R D E R I N G

    override fun compareTo(other: RestrainedRole): Int {
        return COMPARATOR.compare(this, other)
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
                { it.restrs }, Comparator.nullsFirst(Comparator.naturalOrder<Restrs>())
            )

        val SET = HashSet<RestrainedRole>()

        @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
        fun make(type: String, restrsXML: String?): RestrainedRole {
            val roleType = make(type)
            val restrs = if (restrsXML.isNullOrEmpty() || restrsXML == "<SELRESTRS/>") null else Restrs.make(restrsXML, false)
            val r = RestrainedRole(roleType, restrs)
            SET.add(r)
            return r
        }
    }
}
