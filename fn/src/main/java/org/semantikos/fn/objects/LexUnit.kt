package org.semantikos.fn.objects

import edu.berkeley.icsi.framenet.FrameLUType
import edu.berkeley.icsi.framenet.LexUnitDocument
import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasID
import org.semantikos.common.Insertable
import org.semantikos.common.Logger
import org.semantikos.common.Utils.escape
import org.semantikos.common.Utils.nullableQuotedChar
import org.semantikos.common.Utils.nullableQuotedEscapedString
import org.semantikos.fn.FnModule
import org.semantikos.fn.types.FeType
import org.semantikos.fn.types.FeType.getSqlId
import java.io.Serializable
import java.util.*

class LexUnit : HasID, Insertable, Serializable {

    val iD: Int

    val name: String

    private val pos: Int

    val frameID: Int

    val frameName: String

    private val definition: String?

    private val dict: Char?

    private val incorporatedFE: String?

    private val totalAnnotated: Int

    private constructor(lu: LexUnitDocument.LexUnit) {
        iD = lu.getID()
        name = lu.getName()
        pos = lu.getPOS().intValue()
        val def = Definition.Companion.make(lu.getDefinition())
        definition = def.text
        dict = def.dict
        incorporatedFE = lu.getIncorporatedFE()
        totalAnnotated = lu.getTotalAnnotated()
        frameID = lu.getFrameID()
        frameName = lu.getFrame()
    }

    private constructor(lu: FrameLUType, frameid: Int, frameName0: String) {
        iD = lu.getID()
        name = lu.getName()
        pos = lu.getPOS().intValue()
        val def = Definition.Companion.make(lu.getDefinition())
        definition = def.text
        dict = def.dict
        incorporatedFE = lu.getIncorporatedFE()
        totalAnnotated = 0
        frameID = frameid
        frameName = frameName0
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as LexUnit
        return iD == that.iD
    }

    override fun hashCode(): Int {
        return Objects.hash(iD)
    }

    // I N S E R T

    @RequiresIdFrom(type = FeType::class)
    override fun dataRow(): String {
        return "$iD,'${escape(name)}',$pos,${nullableQuotedEscapedString(definition)},${nullableQuotedChar(dict)},${getSqlId(incorporatedFE)},$totalAnnotated,$frameID"
    }

    override fun comment(): String {
        return "frame=$frameName,incfe=$incorporatedFE"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[LU luid=$iD lu=$name frameid=$frameID frame=$frameName]"
    }

    // D E F I N I T I O N

    class Definition(
        val dict: Char?,
        val text: String?,
    ) {

        override fun toString(): String {
            return "$dict|<$text>"
        }

        companion object {

            fun make(definition0: String): Definition {
                var dict: Char? = null
                var definition: String? = definition0
                if (definition0.startsWith("COD")) {
                    dict = 'O'
                    definition = definition0.substring(3)
                }
                if (definition0.startsWith("FN")) {
                    dict = 'F'
                    definition = definition0.substring(2)
                }
                 if (definition != null) {
                    definition = definition.replace("[ \t\n.:]*$|^[ \t\n.:]*".toRegex(), "")
                }
                return Definition(dict, definition)
            }
        }
    }

    companion object {

        val COMPARATOR: Comparator<LexUnit> = Comparator
            .comparing<LexUnit, String> { it.name }
            .thenComparing<Int> { it.iD }

        val SET = HashSet<LexUnit>()

        fun make(lu: LexUnitDocument.LexUnit): LexUnit {
            val u = LexUnit(lu)

            val isNew: Boolean = SET.add(u)
            if (!isNew) {
                Logger.instance.logWarn(FnModule.MODULE_ID, lu.domNode.nodeName, "lu-duplicate", u.toString())
            }
            return u
        }

        fun make(lu: FrameLUType, frameid: Int, frameName: String): LexUnit {
            val u = LexUnit(lu, frameid, frameName)

            val isNew: Boolean = SET.add(u)
            if (!isNew) {
                Logger.instance.logWarn(FnModule.MODULE_ID, lu.domNode.nodeName, "lu-duplicate", u.toString())
            }
            return u
        }
    }
}
