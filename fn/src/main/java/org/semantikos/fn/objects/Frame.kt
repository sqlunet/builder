package org.semantikos.fn.objects

import edu.berkeley.icsi.framenet.FrameDocument
import org.semantikos.common.HasID
import org.semantikos.common.Insertable
import org.semantikos.common.Utils.escape
import org.semantikos.fn.collectors.FnFrameXmlProcessor
import org.xml.sax.SAXException
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class Frame private constructor(
    frame: FrameDocument.Frame,
) : HasID, Insertable {

    val iD: Int = frame.getID()

    val name: String = frame.getName()

    val definition: String

    init {
        try {
            definition = definitionProcessor.process(frame.getDefinition())
        } catch (e: ParserConfigurationException) {
            System.err.println(frame.getDefinition())
            throw e
        } catch (e: SAXException) {
            System.err.println(frame.getDefinition())
            throw e
        } catch (e: IOException) {
            System.err.println(frame.getDefinition())
            throw e
        }
    }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Frame
        return iD == that.iD
    }

    override fun hashCode(): Int {
        return Objects.hash(iD)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "$iD,'${escape(name)}','${escape(definition)}'"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FRAME id=$iD name=$name]"
    }

    companion object {

        val COMPARATOR: Comparator<Frame> = Comparator
            .comparing<Frame, String> { it.name }
            .thenComparing { it.iD }

        val SET = HashSet<Frame>()

        private val definitionProcessor = FnFrameXmlProcessor()

        @Throws(IOException::class, SAXException::class, ParserConfigurationException::class)
        fun make(frame: FrameDocument.Frame): Frame {
            val f = Frame(frame)
            SET.add(f)
            return f
        }
    }
}
