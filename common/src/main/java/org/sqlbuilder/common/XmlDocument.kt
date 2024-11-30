package org.sqlbuilder.common

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.EntityResolver
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.io.StringReader
import java.net.URI
import java.net.URISyntaxException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.XPathExpressionException

open class XmlDocument(filePath: String) {

    @JvmField
    var document: Document = load(filePath)

    val fileName: String?
        get() {
            val uriString = document.documentURI
            if (uriString != null) {
                try {
                    val uri = URI(uriString)
                    val path = uri.getPath()
                    val file = File(path)
                    return file.getName()
                } catch (_: URISyntaxException) {

                }
            }
            return null
        }

    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    private fun load(filePath: String): Document {
        val builder: DocumentBuilder = makeDocumentBuilder()
        builder.setEntityResolver(EntityResolver { publicId: String?, systemId: String? -> InputSource(StringReader("")) })
        return builder.parse(filePath)
    }

    companion object {

        @Throws(ParserConfigurationException::class)
        private fun makeDocumentBuilder(): DocumentBuilder {
            val factory = DocumentBuilderFactory.newInstance()
            factory.isCoalescing = true
            factory.isIgnoringComments = true
            factory.isNamespaceAware = false
            factory.isIgnoringElementContentWhitespace = true
            factory.isExpandEntityReferences = false
            factory.isValidating = false
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
            factory.setFeature("http://xml.org/sax/features/validation", false)
            return factory.newDocumentBuilder()
        }
     }
}
