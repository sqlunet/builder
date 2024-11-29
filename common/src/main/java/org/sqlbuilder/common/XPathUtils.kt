package org.sqlbuilder.common

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
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpressionException
import javax.xml.xpath.XPathFactory

object XPathUtils {

    fun NodeList.genElements() = iterator {
        var i = 0
        while (i < length) {
            yield(item(i))
            i++
        }
    }

    fun genElementsFromNullableNodeList(nodeList: NodeList?) = iterator {
        var i = 0
        while (i < (nodeList?.length ?: 0)) {
            yield(nodeList?.item(i))
            i++
        }
    }

    @JvmStatic
    @Throws(XPathExpressionException::class)
    fun getXPath(start: Node, xpathExpr: String): Node? {
        val xpath = XPathFactory.newInstance().newXPath()
        return xpath.evaluate(xpathExpr, start, XPathConstants.NODE) as Node?
    }

    @JvmStatic
    @Throws(XPathExpressionException::class)
    fun getXPaths(start: Node, xpathExpr: String): NodeList? {
        val xpath = XPathFactory.newInstance().newXPath()
        return xpath.evaluate(xpathExpr, start, XPathConstants.NODESET) as NodeList?
    }

    // X M L   A S   T E X T

    @Throws(TransformerException::class)
    fun getXML(nodes: NodeList): MutableList<String> {
        val result = ArrayList<String>()
        for (i in 0..<nodes.length) {
            result.add(getXML(nodes.item(i)))
        }
        return result
    }

    @JvmStatic
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
