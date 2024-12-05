package org.sqlbuilder.common

import org.junit.BeforeClass
import org.junit.Test
import org.sqlbuilder.common.Progress.trace
import org.sqlbuilder.common.Progress.traceTailer
import org.sqlbuilder.common.XPathUtils.getXML
import org.sqlbuilder.common.XPathUtils.getXPaths
import org.sqlbuilder.common.XmlProcessor.Companion.iteratorOfElements
import org.w3c.dom.Element
import java.io.File
import java.io.FilenameFilter
import javax.xml.transform.TransformerException
import javax.xml.xpath.XPathExpressionException

class TestXPath {

    @Throws(XPathExpressionException::class, TransformerException::class)
    fun testXPath(document: XmlDocument) {
        val nodes = getXPaths(document.document, xPath)
        if (nodes != null) {
            for (i in 0..<nodes.length) {
                val roleElement = nodes.item(i) as Element
                val type = roleElement.getAttribute("type")
                println(type + " " + roleElement.getXML())
            }
        }
    }

    @Throws(XPathExpressionException::class, TransformerException::class)
    fun testXPathElements(document: XmlDocument) {
        getXPaths(document.document, xPath)
            ?.iteratorOfElements()
            ?.forEach {
                val type = it.getAttribute("type")
                println(type + " " + it.getXML())
            }

    }

    @Test
    @Throws(XPathExpressionException::class, TransformerException::class)
    fun testXPath() {
        runFor { testXPath(XmlDocument(it)) }
    }

    @Test
    @Throws(XPathExpressionException::class, TransformerException::class)
    fun testXPathElements() {
        runFor { testXPathElements(XmlDocument(it)) }
    }

    companion object {

        private lateinit var home: String

        private lateinit var  xPath: String

        fun runFor(process: (path: String) -> Unit) {
            val filter = FilenameFilter { _, name -> name.endsWith(".xml") }
            val files = File(home).listFiles(filter)
            if (files == null) {
                throw RuntimeException("Dir:$home is empty")
            }
            // iterate
            var fileCount = 0
            println("reading files")
            files
                .asSequence()
                .sortedWith(Comparator.comparing<File, String> { it.name })
                .forEach {
                    fileCount++
                    process(it.absolutePath)
                    trace(fileCount.toLong())
                }
            traceTailer(fileCount.toLong())
        }

        @BeforeClass
        fun init() {
            home = System.getenv("TEST_HOME")
            xPath = System.getenv("TEST_XPATH")
        }
    }
}
