package org.semantikos.fn.collectors

import edu.berkeley.icsi.framenet.FrameDocument
import org.apache.xmlbeans.XmlException
import org.semantikos.common.Logger
import org.semantikos.fn.FnModule
import org.semantikos.fn.joins.FE_FEExcluded
import org.semantikos.fn.joins.FE_FERequired
import org.semantikos.fn.joins.FE_SemType
import org.semantikos.fn.joins.Frame_FrameRelated.Companion.make
import org.semantikos.fn.joins.Frame_SemType
import org.semantikos.fn.joins.LexUnit_SemType.Companion.make
import org.semantikos.fn.objects.FE.Companion.make
import org.semantikos.fn.objects.Frame.Companion.make
import org.semantikos.fn.objects.LexUnit.Companion.make
import org.semantikos.fn.objects.Lexeme.Companion.make
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class FnFrameCollector(props: Properties) : FnCollector("frame", props, "frame") {

    private val skipLexUnits: Boolean = props.getProperty("fn_skip_lu", "true").compareTo("true", ignoreCase = true) == 0

    override fun processFrameNetFile(fileName: String) {
        val xmlFile = File(fileName)
        try {
            val document = FrameDocument.Factory.parse(xmlFile)

            // F R A M E
            val f = document.getFrame()
            val frameid = f.getID()
            make(f)

            // S E M T Y P E
            f.getSemTypeArray()
                .forEach {
                    Frame_SemType.make(frameid, it.getID())
                }

            // F E C O R E S E T S
            val feToCoresetMap: Map<Int, Int> =
                f.getFEcoreSetArray()
                    .withIndex()
                    .map { (setId, fecoreset) -> setId + 1 to fecoreset }
                    .flatMap { (setId, fecoreset) ->
                        fecoreset
                            .getMemberFEArray()
                            .map {
                                val feid = it.getID()
                                feid to setId
                            }
                    }
                    .toMap()

            // F E
            f.getFEArray()
                .forEach { fe ->
                    val feid = fe.getID()
                    make(fe, feToCoresetMap[feid], frameid)

                    // s e m t y p e s
                    fe.getSemTypeArray()
                        .forEach {
                            FE_SemType.make(feid, it.getID())
                        }

                    // r e q u i r e s
                    fe.getRequiresFEArray()
                        .forEach {
                            FE_FERequired.make(feid, it)
                        }

                    // e x c l u d e s / r e q u i r e s
                    fe.getExcludesFEArray()
                        .forEach {
                            FE_FEExcluded.make(feid, it)
                        }
                }

            // R E L A T E D F R A M E S
            f.getFrameRelationArray()
                .forEach { relations ->
                    val relation = relations.getType()
                    relations.getRelatedFrameArray()
                        .forEach {
                            make(frameid, it, relation)
                        }
                }

            // L E X U N I T S
            if (!skipLexUnits) {
                f.getLexUnitArray()
                    .forEach { lexunit ->
                        val luid = lexunit.getID()
                        make(lexunit, frameid, f.getName())

                        // lexemes
                        lexunit.getLexemeArray()
                            .forEach {
                                make(it, luid.toLong())
                            }

                        // semtypes
                        lexunit.getSemTypeArray()
                            .forEach {
                                make(luid, it)
                            }
                    }
            }
        } catch (e: XmlException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        } catch (e: ParserConfigurationException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        } catch (e: SAXException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        } catch (e: IOException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        } catch (e: RuntimeException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        }
    }
}
