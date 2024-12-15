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

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as SemType
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

        val COMPARATOR: Comparator<SemType> = Comparator
            .comparing<SemType, String> { it.name }
            .thenComparing<Int> { it.iD }

        val SET = HashSet<SemType>()

        fun make(type: SemTypeType): SemType {
            val t = SemType(type)
            SET.add(t)
            return t
        }
    }
}
