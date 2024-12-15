package org.semantikos.fn.collectors

import edu.berkeley.icsi.framenet.LexUnitDocument
import org.apache.xmlbeans.XmlException
import org.semantikos.common.Logger
import org.semantikos.fn.FnModule
import org.semantikos.fn.objects.FERealization
import org.semantikos.fn.objects.LexUnit.Companion.make
import org.semantikos.fn.objects.ValenceUnit
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
