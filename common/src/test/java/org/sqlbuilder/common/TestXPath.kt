package org.sqlbuilder.common

import org.junit.BeforeClass
import org.junit.Test
import org.sqlbuilder.common.XPathUtils.asSequenceOfElements
import org.sqlbuilder.common.XPathUtils.getXML
import org.sqlbuilder.common.XPathUtils.getXPaths
import org.w3c.dom.Element
import javax.xml.transform.TransformerException
import javax.xml.xpath.XPathExpressionException

class TestXPath {

    @Test
    @Throws(XPathExpressionException::class, TransformerException::class)
    fun testXPath() {
        val nodes = getXPaths(document!!.document, xPath)
        if (nodes != null) {
            for (i in 0..<nodes.length) {
                val roleElement = nodes.item(i) as Element
                val type = roleElement.getAttribute("type")
                println(type + " " + getXML(roleElement))
            }
        }
    }

    @Test
    @Throws(XPathExpressionException::class, TransformerException::class)
    fun testXPathElements() {
        getXPaths(document.document, xPath)
            ?.asSequenceOfElements()
            ?.forEach {
                val type = it.getAttribute("type")
                println(type + " " + getXML(it))
            }

    }

    companion object {

        private const val xPath = "//THEMROLES/THEMROLE"

        private lateinit var document: XmlDocument

        @BeforeClass
        @JvmStatic
        fun init() {
            val filePath = System.getenv("TEST")
            document = XmlDocument(filePath)
        }
    }
}
