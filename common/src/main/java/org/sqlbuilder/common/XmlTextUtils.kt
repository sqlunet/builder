package org.sqlbuilder.common

import org.sqlbuilder.common.XPathUtils.getXPaths
import org.w3c.dom.Element
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
import javax.xml.xpath.XPathExpressionException

object XmlTextUtils {

    @Throws(XPathExpressionException::class)
    fun getXPathText(start: Node, xpathExpr: String): String? {
        val node = XPathUtils.getXPath(start, xpathExpr)
        val element = node as Element
        return element.textContent.trim { it <= ' ' }
    }

    @Throws(XPathExpressionException::class)
    fun getXPathTexts(start: Node, xpathExpr: String): List<String>? {
        var result: MutableList<String>? = null
        val nodes: NodeList? = getXPaths(start, xpathExpr)
        if (nodes != null) {
            for (i in 0..<nodes.length) {
                if (result == null) {
                    result = ArrayList<String>()
                }
                val element = nodes.item(i) as Element
                var text = element.textContent.trim { it <= ' ' }
                text = text.replaceFirst("^\"*".toRegex(), "")
                text = text.replaceFirst("\"*$".toRegex(), "")
                text = text.replaceFirst("[.;]*$".toRegex(), "")
                result.add(text)
            }
        }
        return result
    }

    // X M L   A S   T E X T

    @JvmStatic
    @Throws(TransformerException::class)
    fun getXML(nodes: NodeList): MutableList<String> {
        val result = ArrayList<String>()
        for (i in 0..<nodes.length) {
            result.add(getXML(nodes.item(i)))
        }
        return result
    }

    @Throws(TransformerException::class)
    fun getXML(node: Node): String {
        // output stream
        val outStream = StringWriter()
        val resultStream: Result = StreamResult(outStream)

        // source
        val source: Source = DOMSource(node)

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
