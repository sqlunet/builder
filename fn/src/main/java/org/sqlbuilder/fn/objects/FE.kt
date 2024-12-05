package org.sqlbuilder.fn.objects

import edu.berkeley.icsi.framenet.FEType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasID
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.fn.collectors.FnFEXmlProcessor
import org.sqlbuilder.fn.types.FeType
import org.sqlbuilder.fn.types.FeType.getSqlId
import org.xml.sax.SAXException
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class FE private constructor(
    fe: FEType,
    private val coreset: Int?,
    val frameID: Int,
) : HasID, Insertable {

    val iD: Int = fe.getID()

    val name: String = fe.getName()

    val abbrev: String = fe.getAbbrev()

    private val coretype: Int = fe.getCoreType().intValue()

    val definition: String

    init {
        try {
            definition = definitionProcessor.process(fe.getDefinition())
        } catch (e: ParserConfigurationException) {
            System.err.println(fe.getDefinition())
            throw e
        } catch (e: SAXException) {
            System.err.println(fe.getDefinition())
            throw e
        } catch (e: IOException) {
            System.err.println(fe.getDefinition())
            throw e
        }
        FeType.COLLECTOR.add(fe.getName())
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as FE
        return this.iD == that.iD
    }

    override fun hashCode(): Int {
        return Objects.hash(this.iD)
    }

    // I N S E R T

    @RequiresIdFrom(type = FeType::class)
    override fun dataRow(): String {
        return "$iD,${getSqlId(name)},'${escape(abbrev)}','${escape(definition)}',$coretype,${nullableInt(coreset)},$frameID"
    }

    override fun comment(): String {
        return "type=$name"
    }

    override fun toString(): String {
        return "[FE feid=$iD name=$name frameid=$frameID]"
    }

    companion object {

        val COMPARATOR: Comparator<FE> = Comparator
            .comparing<FE, String> { it.name }
            .thenComparing<Int> { it.iD }

        val SET = HashSet<FE>()

        var BY_FETYPEID_AND_FRAMEID: Map<Pair<Int, Int>, FE>? = null

        private val definitionProcessor = FnFEXmlProcessor()

        @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
        fun make(fe: FEType, coreset: Int?, frameid: Int): FE {
            val e = FE(fe, coreset, frameid)
            SET.add(e)
            return e
        }
    }
}
