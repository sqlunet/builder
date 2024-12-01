package org.sqlbuilder.fn.collectors

import edu.berkeley.icsi.framenet.LexUnitDocument
import org.apache.xmlbeans.XmlException
import org.sqlbuilder.common.Logger
import org.sqlbuilder.fn.FnModule
import org.sqlbuilder.fn.objects.FERealization
import org.sqlbuilder.fn.objects.LexUnit.Companion.make
import org.sqlbuilder.fn.objects.ValenceUnit
import java.io.File
import java.io.IOException
import java.util.*

class FnLexUnitExportCollector(props: Properties) : FnCollector("lu", props, "lu") {

    private val vuToFer: MutableMap<ValenceUnit?, FERealization?> = HashMap<ValenceUnit?, FERealization?>()

    override fun processFrameNetFile(fileName: String) {
        vuToFer.clear()

        val file = File(fileName)
        try {
            val document = LexUnitDocument.Factory.parse(file)

            // L E X U N I T
            val lexunit = document.getLexUnit()
            make(lexunit)
        } catch (e: XmlException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        } catch (e: IOException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        }
    }
}
