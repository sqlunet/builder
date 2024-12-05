package org.sqlbuilder.vn.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.vn.collector.VnSyntaxXmlProcessor
import org.xml.sax.SAXException
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class Syntax private constructor(
    val syntax: String,
) : HasId, Insertable, Comparable<Syntax> {

    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Syntax
        return syntax == that.syntax
    }

    override fun hashCode(): Int {
        return Objects.hash(syntax)
    }

    // O R D E R I N G

    override fun compareTo(that: Syntax): Int {
        return syntax.compareTo(that.syntax)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'${escape(syntax)}'"
    }

    companion object {

        val COMPARATOR: Comparator<Syntax> = Comparator.comparing<Syntax, String> { it.syntax }

        @JvmField
        val COLLECTOR: SetCollector<Syntax> = SetCollector<Syntax>(COMPARATOR)

        private val SYNTAX_XML_PROCESSOR = VnSyntaxXmlProcessor()

        @JvmStatic
        @Throws(IOException::class, SAXException::class, ParserConfigurationException::class)
        fun make(syntax: String): Syntax {
            var syntax2 = syntax.replaceFirst("^<SYNTAX>".toRegex(), "").replaceFirst("</SYNTAX>$".toRegex(), "")
            try {
                syntax2 = SYNTAX_XML_PROCESSOR.process(syntax2)
                val s = Syntax(syntax2)
                COLLECTOR.add(s)
                return s
            } catch (e: ParserConfigurationException) {
                System.err.println(syntax2)
                throw e
            } catch (e: IOException) {
                System.err.println(syntax2)
                throw e
            } catch (e: SAXException) {
                System.err.println(syntax2)
                throw e
            }
        }
    }
}
