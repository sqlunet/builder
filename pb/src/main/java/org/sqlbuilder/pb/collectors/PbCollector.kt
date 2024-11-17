package org.sqlbuilder.pb.collectors

import org.sqlbuilder.common.Logger
import org.sqlbuilder.common.Processor
import org.sqlbuilder.common.Progress
import org.sqlbuilder.common.XmlDocument
import org.sqlbuilder.pb.PbModule
import org.sqlbuilder.pb.collectors.PbDocument.Companion.getAliasPredicates
import org.sqlbuilder.pb.collectors.PbDocument.Companion.getPredicates
import org.sqlbuilder.pb.collectors.PbDocument.Companion.makeExampleArgs
import org.sqlbuilder.pb.collectors.PbDocument.Companion.makeExamples
import org.sqlbuilder.pb.collectors.PbDocument.Companion.makeRoleSets
import org.sqlbuilder.pb.collectors.PbDocument.Companion.makeRoles
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.File
import java.io.FilenameFilter
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.XPathExpressionException

open class PbCollector(conf: Properties) : Processor("pb") {

    @JvmField
    protected val propBankHome: String = conf.getProperty("pb_home", System.getenv()["PBHOME"])

    override fun run() {
        val folder = File(this.propBankHome)
        val filter = FilenameFilter { dir: File, name: String -> name.endsWith(".xml") }
        val fileArray = folder.listFiles(filter)
        if (fileArray == null) {
            throw RuntimeException("Dir:" + this.propBankHome + " is empty")
        }
        Progress.traceHeader("propbank", "reading files")
        var fileCount = 0
        listOf<File>(*fileArray)
            .sortedWith(Comparator.comparing<File, String> { it!!.name })
            .forEach {
                fileCount++
                processPropBankFile(it.absolutePath, it.name)
                Progress.trace(fileCount.toLong())
            }
        Progress.traceTailer(fileCount.toLong())
    }

    open fun processPropBankFile(fileName: String, name: String) {
        val head: String = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        try {
            val document = PbDocument(fileName)
            processFrameset(document, XmlDocument.getXPath(document.getDocument(), "./frameset"), head)
        } catch (e: ParserConfigurationException) {
            Logger.instance.logXmlException(PbModule.MODULE_ID, tag, fileName, e)
        } catch (e: SAXException) {
            Logger.instance.logXmlException(PbModule.MODULE_ID, tag, fileName, e)
        } catch (e: XPathExpressionException) {
            Logger.instance.logXmlException(PbModule.MODULE_ID, tag, fileName, e)
        } catch (e: IOException) {
            Logger.instance.logXmlException(PbModule.MODULE_ID, tag, fileName, e)
        }
    }

    protected open fun processFrameset(document: PbDocument, start: Node, head: String) {
        try {
            // predicates
            val predicates = getPredicates(head, start)
            for (predicate in predicates) {
                try {
                    predicate.put()
                } catch (_: RuntimeException) {
                    // Logger.logger.logException(PbModule.id, this.logTag, "predicate", document.getFileName(), -1, "predicate-duplicate", e);
                }
            }

            val aliasLexItems = getAliasPredicates(start)
            for (lexItem in aliasLexItems) {
                try {
                    lexItem.put()
                } catch (_: RuntimeException) {
                    // Logger.logger.logException(PbModule.id, this.logTag, "lexitem", document.getFileName(), -1, "lexitem-duplicate", e);
                }
            }

            // rolesets
            makeRoleSets(head, start)

            // roles
            makeRoles(head, start)

            // examples
            makeExamples(head, start)

            // args
            makeExampleArgs(head, start)
        } catch (e: XPathExpressionException) {
            Logger.instance.logXmlException(PbModule.MODULE_ID, tag, document.getFileName(), e)
        }
    }
}
