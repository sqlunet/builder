package org.sqlbuilder.common

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

abstract class XmlProcessor {

    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    abstract fun process(xml: String): String

    companion object {

        fun NodeList.iterator() = iterator {
            var i = 0
            while (i < length) {
                yield(item(i))
                i++
            }
        }

        fun NodeList.iteratorOfElements() = iterator {
            var i = 0
            while (i < length) {
                yield(item(i) as Element)
                i++
            }
        }


        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()

        @Throws(IOException::class, SAXException::class, ParserConfigurationException::class)
        fun docFromString(xml: String): Element {
            try {
                val builder: DocumentBuilder = factory.newDocumentBuilder()
                return builder.parse(ByteArrayInputStream(xml.toByteArray())).documentElement
            } catch (e: SAXException) {
                System.err.println(xml)
                throw e
            } catch (e: ParserConfigurationException) {
                System.err.println(xml)
                throw e
            } catch (e: IOException) {
                System.err.println(xml)
                throw e
            }
        }
    }
}
