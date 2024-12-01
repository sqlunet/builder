package org.sqlbuilder.fn.collectors

import edu.berkeley.icsi.framenet.LexUnitDocument
import org.apache.xmlbeans.XmlException
import org.sqlbuilder.common.Logger
import org.sqlbuilder.fn.FnModule
import org.sqlbuilder.fn.objects.Word.Companion.make
import java.io.File
import java.io.IOException
import java.util.*

class FnWordCollector(props: Properties) : FnCollector("lu", props, "w") {

    override fun processFrameNetFile(fileName: String) {
        val file = File(fileName)
        try {
            val document = LexUnitDocument.Factory.parse(file)
            val lu = document.getLexUnit()

            // L E X E M E S
            lu.getLexemeArray()
                .forEach {
                    make(it.name)
                }

            // V A L E N C E S
            // g o v e r n o r s
            lu.getValences().getGovernorArray()
                .forEach {
                    make(it.lemma)
                }
        } catch (e: XmlException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        } catch (e: IOException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        }
    }
}
