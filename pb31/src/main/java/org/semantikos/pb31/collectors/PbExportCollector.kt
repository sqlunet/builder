package org.semantikos.pb31.collectors

import org.semantikos.common.Logger
import org.semantikos.common.Progress
import org.semantikos.common.XPathUtils.getXPath
import org.semantikos.pb31.Pb31Module
import org.semantikos.pb31.collectors.PbDocument.Companion.makeRoleSets
import org.semantikos.pb31.collectors.PbDocument.Companion.makeRoles
import org.w3c.dom.Node
import java.io.File
import java.io.FilenameFilter
import java.util.*
import javax.xml.xpath.XPathExpressionException

class PbExportCollector(conf: Properties) : PbCollector(conf) {

    override fun run() {
        val folder = File(propBankHome)
        val filter = FilenameFilter { dir: File, name: String -> name.endsWith(".xml") }
        val fileArray = folder.listFiles(filter)
        if (fileArray == null) {
            throw RuntimeException("Dir:$propBankHome is empty")
        }
        Progress.traceHeader("propbank", "reading files")
        var fileCount = 0
        listOf<File>(*fileArray)
            .sortedWith(Comparator.comparing<File, String> { it.name })
            .forEach {
                fileCount++
                processPropBankFile(it.absolutePath, it.name)
                Progress.trace(fileCount.toLong())
            }
        Progress.traceTailer(fileCount.toLong())
    }

    override fun processPropBankFile(fileName: String, name: String) {
        val head = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        try {
            val document = PbDocument(fileName)
            processFrameset(document, getXPath(document.document, "./frameset")!!, head)
        } catch (e: XPathExpressionException) {
            Logger.instance.logXmlException(Pb31Module.MODULE_ID, tag, fileName, e)
        }
    }

    override fun processFrameset(document: PbDocument, start: Node, head: String) {
        try {
            // rolesets
            makeRoleSets(head, start)

            // roles
            makeRoles(head, start)
        } catch (e: XPathExpressionException) {
            Logger.instance.logXmlException(Pb31Module.MODULE_ID, tag, document.fileName, e)
        }
    }
}
