package org.sqlbuilder.fn.collectors

import edu.berkeley.icsi.framenet.SemTypesDocument
import org.apache.xmlbeans.XmlException
import org.sqlbuilder.common.Logger
import org.sqlbuilder.fn.FnModule
import org.sqlbuilder.fn.joins.SemType_SemTypeSuper.Companion.make
import org.sqlbuilder.fn.objects.SemType.Companion.make
import java.io.File
import java.io.IOException
import java.util.*

class FnSemTypeCollector(filename: String, props: Properties) : FnCollector1(filename, props, "semtype") {

    override fun processFrameNetFile(fileName: String) {
        val xmlFile = File(fileName)
        try {
            val document = SemTypesDocument.Factory.parse(xmlFile)

            document.getSemTypes().getSemTypeArray()
                .forEach { t ->
                    val semtype = make(t)
                    t.getSuperTypeArray()
                        .forEach {
                            make(semtype, it)
                        }
                }
        } catch (e: XmlException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        } catch (e: IOException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        }
    }
}
