package org.semantikos.fn.objects

import edu.berkeley.icsi.framenet.SemTypeType
import org.semantikos.common.HasID
import org.semantikos.common.Insertable
import org.semantikos.common.Utils.escape
import java.util.*

class SemType private constructor(
    type: SemTypeType,
) : HasID, Insertable {

    val iD: Int = type.getID()

    val name: String = type.getName()

    private val abbrev: String = type.getAbbrev()

    private val definition: String = type.getDefinition()

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as SemType
        return iD == that.iD
    }

    override fun hashCode(): Int {
        return Objects.hash(iD)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "$iD,'$name','$abbrev','${escape(definition)}'"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[SEMTYPE semtypeid=$iD name=$name]"
    }

    companion object {

        val COMPARATOR: Comparator<SemType> = compareBy<SemType> { it.name }
            .thenBy { it.iD }

        val SET = HashSet<SemType>()

        fun make(type: SemTypeType): SemType {
            val t = SemType(type)
            SET.add(t)
            return t
        }
    }
}
