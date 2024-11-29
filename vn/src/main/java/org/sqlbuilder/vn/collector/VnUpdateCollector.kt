package org.sqlbuilder.vn.collector

import org.sqlbuilder.common.Logger
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.traceHeader
import org.sqlbuilder.common.Progress.traceTailer
import org.sqlbuilder.common.XPathUtils.getXPath
import org.sqlbuilder.common.XPathUtils.getXPaths
import org.sqlbuilder.vn.VnDocument
import org.sqlbuilder.vn.VnModule
import org.sqlbuilder.vn.objects.Word.Companion.make
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.File
import java.io.FilenameFilter
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.XPathExpressionException

class VnUpdateCollector(props: Properties) : VnCollector(props) {

    override fun run() {
        val folder = File(this.verbNetHome)
        val filter = FilenameFilter { dir: File, name: String -> name.endsWith(".xml") }
        val files = folder.listFiles(filter)
        if (files == null) {
            throw RuntimeException("Dir:" + this.verbNetHome + " is empty")
        }
        // iterate
        var fileCount = 0
        traceHeader("reading verbnet files", "")
        files
            .sortedWith(Comparator.comparing<File, String> { it.name })
            .forEach {
                fileCount++
                processVerbNetFile(it.absolutePath, it.name)
            }
        traceTailer(fileCount.toLong())
    }

    override fun processVerbNetFile(fileName: String, name: String) {
        val head = name.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        try {
            val document = VnDocument(fileName)
            processVerbNetClass(getXPath(document.document, "./VNCLASS")!!, head)
        } catch (e: ParserConfigurationException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, fileName, e)
            traceDone(e.toString())
        } catch (e: SAXException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, fileName, e)
            traceDone(e.toString())
        } catch (e: IOException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, fileName, e)
            traceDone(e.toString())
        } catch (e: XPathExpressionException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, fileName, e)
            traceDone(e.toString())
        }
    }

    fun processVerbNetClass(start: Node, head: String) {
        try {
            processMembers(start, head)

            // recurse
            val subclasses = getXPaths(start, "./SUBCLASSES/VNSUBCLASS")!!
            for (i in 0..<subclasses.length) {
                val subNode = subclasses.item(i)
                processVerbNetClass(subNode, head)
            }
        } catch (e: XPathExpressionException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, start.ownerDocument.documentURI, e)
        }
    }

    companion object {

        @Throws(XPathExpressionException::class)
        private fun processMembers(start: Node, head: String) {
            make(head)
            VnDocument.makeResolvableMembers(start)
        }
    }
}
