package org.sqlbuilder.fn.collectors

import edu.berkeley.icsi.framenet.FullTextAnnotationDocument
import org.apache.xmlbeans.XmlException
import org.sqlbuilder.common.AlreadyFoundException
import org.sqlbuilder.common.Logger
import org.sqlbuilder.fn.FnModule
import org.sqlbuilder.fn.objects.AnnotationSet.Companion.make
import org.sqlbuilder.fn.objects.Corpus.Companion.make
import org.sqlbuilder.fn.objects.Doc.Companion.make
import org.sqlbuilder.fn.objects.Label
import org.sqlbuilder.fn.objects.Layer.Companion.make
import org.sqlbuilder.fn.objects.Sentence.Companion.make
import java.io.File
import java.io.IOException
import java.util.*

class FnFullTextCollector(props: Properties) : FnCollector("fulltext", props, "fulltext") {

    override fun processFrameNetFile(fileName: String) {
        val xmlFile = File(fileName)
        try {
            val document = FullTextAnnotationDocument.Factory.parse(xmlFile)

            // F U L L T E X T A N N O T A T I O N
            val fta = document.getFullTextAnnotation()

            // H E A D E R
            val h = fta.getHeader()

            // F R A M E
            val f = h.getFrame()
            f?.getFEArray()
                ?.forEach {
                    System.err.println("${it.name} ${it.type}")
                }

            // C O R P U S
            h.getCorpusArray()
                .forEach { c ->
                    make(c, null)
                    c.getDocumentArray()
                        .forEach {
                            make(it, c)
                        }
                }

            // S E N T E N C E S
            fta.getSentenceArray()
                .forEach { s ->
                    val sentenceid = s.getID()
                    make(s)

                    // annotation sets
                    s.getAnnotationSetArray()
                        .forEach { a ->
                            try {
                                make(a, sentenceid)

                                // layers
                                a.getLayerArray()
                                    .forEach { l ->
                                        val layer = make(l, a.getID())

                                        // labels
                                        l.getLabelArray()
                                            .forEach {
                                                Label.make(it, layer)
                                            }
                                    }
                            } catch (_: AlreadyFoundException) {
                                // ignore
                            }

                        }
                }
        } catch (e: XmlException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        } catch (e: IOException) {
            Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e)
        }
    }
}
