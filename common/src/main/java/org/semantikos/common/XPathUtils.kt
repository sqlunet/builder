package org.semantikos.common

import org.semantikos.common.XmlProcessor.Companion.iteratorOfElements
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.StringWriter
import javax.xml.XMLConstants
import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpressionException
import javax.xml.xpath.XPathFactory

object XPathUtils {

    @Throws(XPathExpressionException::class)
    fun getXPath(start: Node, xpathExpr: String): Node? {
        val xpath = XPathFactory.newInstance().newXPath()
        return xpath.evaluate(xpathExpr, start, XPathConstants.NODE) as Node?
    }

    @Throws(XPathExpressionException::class)
    fun getXPaths(start: Node, xpathExpr: String): NodeList? {
        val xpath = XPathFactory.newInstance().newXPath()
        return xpath.evaluate(xpathExpr, start, XPathConstants.NODESET) as NodeList?
    }

    // X M L   A S   T E X T

    @Throws(TransformerException::class)
    fun NodeList.getXML(): List<String> {
        return iteratorOfElements()
            .asSequence()
            .map { it.getXML() }
            .toList()
    }

    @Throws(TransformerException::class)
    fun Node.getXML(): String {

        // output stream
        val outStream = StringWriter()
        val resultStream: Result = StreamResult(outStream)

        // source
        val source: Source = DOMSource(this)

        // output
        val factory = TransformerFactory.newInstance()
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
        val transformer = factory.newTransformer()
        transformer.transform(source, resultStream)
        var result = outStream.toString()
        val header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        if (result.startsWith(header)) {
            result = result.substring(header.length)
        }
        return result
    }
}
