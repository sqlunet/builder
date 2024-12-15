package org.semantikos.vn.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import org.semantikos.vn.collector.VnRestrsXmlProcessor
import org.xml.sax.SAXException
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class Restrs private constructor(
    val value: String,
    val isSyntactic: Boolean,
) : HasId, Insertable, Comparable<Restrs> {

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
        val that = o as Restrs
        return isSyntactic == that.isSyntactic && value == that.value
    }

    override fun hashCode(): Int {
        return Objects.hash(value, isSyntactic)
    }

    // O R D E R I N G

    override fun compareTo(that: Restrs): Int {
        val cmp = value.compareTo(that.value)
        if (cmp != 0) {
            return cmp
        }
        return java.lang.Boolean.compare(isSyntactic, that.isSyntactic)
    }

    // S T R I N G

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(value)
        if (isSyntactic) {
            sb.append('*')
        }
        return sb.toString()
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$value',$isSyntactic"
    }

    companion object {

        private const val LOG_ONLY = false

        val COMPARATOR: Comparator<Restrs> = Comparator.comparing<Restrs, String> { it.value }
            .thenComparing<Boolean> { it.isSyntactic }

        val COLLECTOR = SetCollector<Restrs>(COMPARATOR)

        private val RESTRS_XML_PROCESSOR = VnRestrsXmlProcessor()

        @Throws(IOException::class, SAXException::class, ParserConfigurationException::class)
        fun make(value: String, isSyntactic: Boolean): Restrs {
            try {
                val value2 = RESTRS_XML_PROCESSOR.process(value)
                if (value2.isEmpty()) {
                    if (LOG_ONLY) {
                        System.err.println("empty [$value]")
                    } else {
                        throw RuntimeException("empty [$value]")
                    }
                }
                val r = Restrs(value2, isSyntactic)
                COLLECTOR.add(r)
                return r
            } catch (e: ParserConfigurationException) {
                System.err.println(value)
                throw e
            } catch (e: IOException) {
                System.err.println(value)
                throw e
            } catch (e: SAXException) {
                System.err.println(value)
                throw e
            }
        }
    }
}
