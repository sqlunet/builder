package org.sqlbuilder.vn.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.vn.collector.VnSemanticsXmlProcessor
import org.xml.sax.SAXException
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class Semantics private constructor(
    val semantics: String,
) : HasId, Insertable, Comparable<Semantics> {

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
        val that = o as Semantics
        return semantics == that.semantics
    }

    override fun hashCode(): Int {
        return Objects.hash(semantics)
    }

    // O R D E R I N G

    override fun compareTo(that: Semantics): Int {
        return this.semantics.compareTo(that.semantics)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$semantics'"
    }

    companion object {

        val COMPARATOR: Comparator<Semantics> = Comparator.comparing<Semantics, String> { it.semantics }

        @JvmField
        val COLLECTOR: SetCollector<Semantics> = SetCollector<Semantics>(COMPARATOR)

        private val SEMANTICS_PROCESSOR = VnSemanticsXmlProcessor()

        // C O N S T R U C T O R
        @JvmStatic
        @Throws(IOException::class, SAXException::class, ParserConfigurationException::class)
        fun make(semantics: String): Semantics {
            var semantics2 = semantics.replaceFirst("^<SEMANTICS>".toRegex(), "").replaceFirst("</SEMANTICS>$".toRegex(), "")
            try {
                semantics2 = SEMANTICS_PROCESSOR.process(semantics2)
                val s = Semantics(semantics2)
                COLLECTOR.add(s)
                return s
            } catch (e: ParserConfigurationException) {
                System.err.println(semantics2)
                throw e
            } catch (e: IOException) {
                System.err.println(semantics2)
                throw e
            } catch (e: SAXException) {
                System.err.println(semantics2)
                throw e
            }
        }
    }
}
