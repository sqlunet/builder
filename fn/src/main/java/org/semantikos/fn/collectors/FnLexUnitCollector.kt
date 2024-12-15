package org.semantikos.fn.collectors

import edu.berkeley.icsi.framenet.LexUnitDocument
import org.apache.xmlbeans.XmlException
import org.semantikos.common.AlreadyFoundException
import org.semantikos.common.Logger
import org.semantikos.fn.FnModule
import org.semantikos.fn.joins.FEGroupPattern.Companion.make
import org.semantikos.fn.joins.FEGroupPattern_AnnoSet.Companion.make
import org.semantikos.fn.joins.FEGroupPattern_FEPattern.Companion.make
import org.semantikos.fn.joins.FEPattern.Companion.make
import org.semantikos.fn.joins.FE_FEGroupRealization.Companion.make
import org.semantikos.fn.joins.Governor_AnnoSet.Companion.make
import org.semantikos.fn.joins.LexUnit_Governor.Companion.make
import org.semantikos.fn.joins.LexUnit_SemType.Companion.make
import org.semantikos.fn.joins.SubCorpus_Sentence.Companion.make
import org.semantikos.fn.joins.ValenceUnit_AnnoSet.Companion.make
import org.semantikos.fn.objects.AnnotationSet.Companion.make
import org.semantikos.fn.objects.Corpus.Companion.make
import org.semantikos.fn.objects.Doc.Companion.make
import org.semantikos.fn.objects.FEGroupRealization.Companion.make
import org.semantikos.fn.objects.FERealization
import org.semantikos.fn.objects.FERealization.Companion.make
import org.semantikos.fn.objects.Governor.Companion.make
import org.semantikos.fn.objects.Label
import org.semantikos.fn.objects.Layer.Companion.make
import org.semantikos.fn.objects.LexUnit.Companion.make
import org.semantikos.fn.objects.Lexeme.Companion.make
import org.semantikos.fn.objects.Sentence.Companion.make
import org.semantikos.fn.objects.SubCorpus.Companion.make
import org.semantikos.fn.objects.ValenceUnit
import org.semantikos.fn.objects.ValenceUnit.Companion.make
import java.io.File
import java.io.IOException
import java.util.*

class FnLexUnitCollector(props: Properties) : FnCollector("lu", props, "lu") {

    private val skipLayers: Boolean = props.getProperty("fn_skip_layers", "").compareTo("true", ignoreCase = true) == 0

    private lateinit var vuToFer: Map<ValenceUnit, FERealization>

    override fun processFrameNetFile(fileName: String) {

        val file = File(fileName)
        try {
            val document = LexUnitDocument.Factory.parse(file)

            // L E X U N I T
            val lexunit = document.getLexUnit()
            val luid = lexunit.getID()
            val frameid = lexunit.getFrameID()
            make(lexunit)

            // H E A D E R
            lexunit.getHeader().getCorpusArray()
                .forEach { corpus ->
                    make(corpus, luid)
                    corpus.getDocumentArray()
                        .forEach {
                            make(it, corpus)
                        }
                }

            // L E X E M E S
            lexunit.getLexemeArray()
                .forEach {
                    make(it, luid.toLong())
                }

            // S E M T Y P E S
            lexunit.getSemTypeArray()
                .forEach {
                    make(luid, it)
                }

            // V A L E N C E S
            val valences = lexunit.getValences()

            // g o v e r n o r s
            valences.getGovernorArray()
                .forEach { g ->
                    val governor = make(g)
                    make(luid, governor)

                    g.getAnnoSetArray()
                        .forEach {
                            make(governor, it)
                        }
                }

            // F E r e a l i z a t i o n s
            vuToFer = valences.getFERealizationArray()
                .flatMap { r ->
                    val fer = make(r, luid, frameid)

                    // p a t t e r n s
                    r.getPatternArray()
                        .map { p ->
                            // v a l e n c e u n i t
                            val vu = p.getValenceUnit()
                            val valenceunit = make(vu)
                            make(fer, valenceunit)

                            // a n n o s e t s
                            p.getAnnoSetArray()
                                .forEach {
                                    make(valenceunit, it.getID())
                                }
                            valenceunit to fer
                        }
                }
                .toMap()

            // F E g r o u p r e a l i z a t i o n s
            //  <FEGroupRealization>
            //      <FR name="fe1"/>
            //      <FR name="fe2"/>
            //      <FR name="fe3"/>
            //      <pattern total="count(*)">
            //		    <valenceUnit FE="fe1" PT="pt" GF="gf"/>
            //			<valenceUnit FE="fe2" PT="pt" GF="gf"/>
            //			<valenceUnit FE="fe3" PT="pt" GF="gf"/>
            //          <annoSet ID="n"/> *
            //      </pattern>
            //  </FEGroupRealization>
            // The following assumes the grouppatterns reuse the valence units declared in FERealization
            // so we simply point to them through the vuToFer map

            valences.getFEGroupRealizationArray()
                .forEach { gr ->
                    val fegr = make(gr, luid, frameid)

                    // f e s
                    gr.getFEArray()
                        .forEach {
                            make(it, fegr)
                        }

                    // p a t t e r n s
                    gr.getPatternArray()
                        .forEach { gp ->
                            val grouppattern = make(fegr, gp)

                            // v a l e n c e u n i t s
                            gp.getValenceUnitArray()
                                .forEach {
                                    val valenceunit = make(it)
                                    val fer: FERealization = vuToFer[valenceunit]!!
                                    make(grouppattern, fer, valenceunit)
                                }

                            // a n n o s e t s
                            gp.getAnnoSetArray()
                                .forEach {
                                    make(grouppattern, it)
                                }
                        }
                }

            // S U B C O R P U S

            lexunit.getSubCorpusArray()
                .forEach { sc ->
                    val subcorpus = make(sc, luid)

                    sc.getSentenceArray()
                        .forEach { s ->
                            val sentence = make(s)
                            make(subcorpus, sentence)

                            s.getAnnotationSetArray()
                                .forEach { a ->
                                    try {
                                        make(a, s.getID(), luid, lexunit.getFrameID())
                                        if (!skipLayers) {

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
                                        }
                                    } catch (_: AlreadyFoundException) {
                                        // ignore
                                    }
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
