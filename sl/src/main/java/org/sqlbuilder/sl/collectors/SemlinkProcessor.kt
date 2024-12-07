package org.sqlbuilder.sl.collectors

import org.sqlbuilder.common.Logger
import org.sqlbuilder.common.Processor
import org.sqlbuilder.common.Progress.traceHeader
import org.sqlbuilder.common.Progress.traceTailer
import org.sqlbuilder.common.XPathUtils.getXPath
import org.sqlbuilder.sl.SlModule
import org.sqlbuilder.sl.collectors.SemlinkDocument.Companion.makeMappings
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.XPathExpressionException

open class SemlinkProcessor(props: Properties) : Processor("semlink") {

    protected val semlinkFile: String = props.getProperty("sl_home", System.getenv()["SEMLINKHOME"]) + File.separatorChar + props.getProperty("sl_file")

    override fun run() {
        try {
            val document = SemlinkDocument(semlinkFile)
            processSemlinks(getXPath(document.document, "./pbvn-typemap")!!)
        } catch (e: ParserConfigurationException) {
            Logger.instance.logXmlException(SlModule.MODULE_ID, tag, semlinkFile, e)
        } catch (e: SAXException) {
            Logger.instance.logXmlException(SlModule.MODULE_ID, tag, semlinkFile, e)
        } catch (e: XPathExpressionException) {
            Logger.instance.logXmlException(SlModule.MODULE_ID, tag, semlinkFile, e)
        } catch (e: IOException) {
            Logger.instance.logXmlException(SlModule.MODULE_ID, tag, semlinkFile, e)
        }
    }

    companion object {

        @Throws(XPathExpressionException::class)
        private fun processSemlinks(start: Node) {
            traceHeader("semlink", "reading file")
            makeMappings(start)
            traceTailer(1)
        }
    }
}
