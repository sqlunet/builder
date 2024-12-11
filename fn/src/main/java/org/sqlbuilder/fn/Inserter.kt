package org.sqlbuilder.fn

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Insert.insertAndIncrement
import org.sqlbuilder.common.Insert.insertFragmented
import org.sqlbuilder.common.Insert.insertStrings
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.traceSaving
import org.sqlbuilder.fn.joins.*
import org.sqlbuilder.fn.objects.*
import org.sqlbuilder.fn.objects.Values.LabelIType
import org.sqlbuilder.fn.types.*
import org.sqlbuilder.fn.types.FeType.getIntId
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class Inserter(
    conf: Properties,
) {

    protected val names: Names = Names("fn")

    protected var header: String = conf.getProperty("fn_header")

    protected var outDir: File = File(conf.getProperty("fn_outdir", "sql/data"))

    init {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    open fun insert() {
        insertSemTypes()
        insertFrames()
        insertLexUnitsAndText()
    }

    @Throws(FileNotFoundException::class)
    fun insertSemTypes() {
        traceSaving("semtypes")
        insert(SemType.SET, SemType.COMPARATOR, File(outDir, names.file("semtypes")), names.table("semtypes"), names.columns("semtypes"), header)
        SemType.SET.clear()
        traceDone()

        traceSaving("semtype_super")
        insert(SemType_SemTypeSuper.SET, SemType_SemTypeSuper.COMPARATOR, File(outDir, names.file("semtypes_supers")), names.table("semtypes_supers"), names.columns("semtypes_supers"), header)
        SemType_SemTypeSuper.SET.clear()
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    fun insertFrames() {
        traceSaving("frames")
        insert(Frame.SET, Comparator.comparing<Frame, Int?>(Frame::iD), File(outDir, names.file("frames")), names.table("frames"), names.columns("frames"), header)
        Frame.SET.clear()
        traceDone()

        FrameRelation.COLLECTOR.open().use {
            traceSaving("frame_relations")
            insertStrings(FrameRelation.COLLECTOR, FrameRelation.COLLECTOR, File(outDir, names.file("framerelations")), names.table("framerelations"), names.columns("framerelations"), header)
            traceDone()

            traceSaving("frame_related")
            insert(Frame_FrameRelated.SET, Frame_FrameRelated.COMPARATOR, File(outDir, names.file("frames_related")), names.table("frames_related"), names.columns("frames_related"), header)
            Frame_FrameRelated.SET.clear()
            traceDone()
        }
        traceSaving("fe_required")
        insert(FE_FERequired.SET, FE_FERequired.COMPARATOR, File(outDir, names.file("fes_required")), names.table("fes_required"), names.columns("fes_required"), header)
        FE_FERequired.SET.clear()
        traceDone()

        traceSaving("fe_excluded")
        insert(FE_FEExcluded.SET, FE_FEExcluded.COMPARATOR, File(outDir, names.file("fes_excluded")), names.table("fes_excluded"), names.columns("fes_excluded"), header)
        FE_FEExcluded.SET.clear()
        traceDone()

        traceSaving("frame_semtype")
        insert(Frame_SemType.SET, Frame_SemType.COMPARATOR, File(outDir, names.file("frames_semtypes")), names.table("frames_semtypes"), names.columns("frames_semtypes"), header)
        Frame_SemType.SET.clear()
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    fun insertLexUnitsAndText() {
        GfType.COLLECTOR.open().use {
            PtType.COLLECTOR.open().use {
                LayerType.COLLECTOR.open().use {
                    LabelType.COLLECTOR.open().use {

                        traceSaving("maps", "pos,coretype,labelitype")
                        insert(Values.Pos.MAP.keys, { Values.Pos.MAP[it]!! }, File(outDir, names.file("poses")), names.table("poses"), names.columns("poses"), header)
                        insert(Values.CoreType.MAP.keys, { Values.CoreType.MAP[it]!! }, File(outDir, names.file("coretypes")), names.table("coretypes"), names.columns("coretypes"), header)
                        insert(LabelIType.MAP.keys, { LabelIType.MAP[it]!! }, File(outDir, names.file("labelitypes")), names.table("labelitypes"), names.columns("labelitypes"), header)
                        traceDone()

                        traceSaving("gf")
                        insertStrings(GfType.COLLECTOR, GfType.COLLECTOR, File(outDir, names.file("gftypes")), names.table("gftypes"), names.columns("gftypes"), header)
                        traceDone()

                        traceSaving("pt")
                        insertStrings(PtType.COLLECTOR, PtType.COLLECTOR, File(outDir, names.file("pttypes")), names.table("pttypes"), names.columns("pttypes"), header)
                        traceDone()

                        traceSaving("layertypes")
                        insertStrings(LayerType.COLLECTOR, LayerType.COLLECTOR, File(outDir, names.file("layertypes")), names.table("layertypes"), names.columns("layertypes"), header)
                        traceDone()

                        traceSaving("labeltypes")
                        insertStrings(LabelType.COLLECTOR, LabelType.COLLECTOR, File(outDir, names.file("labeltypes")), names.table("labeltypes"), names.columns("labeltypes"), header)
                        traceDone()

                        traceSaving("annoset")
                        insert(AnnotationSet.SET, AnnotationSet.COMPARATOR, File(outDir, names.file("annosets")), names.table("annosets"), names.columns("annosets"), header)
                        AnnotationSet.SET.clear()
                        traceDone()

                        traceSaving("cxns")
                        insert(Cxns.SET, Cxns.COMPARATOR, File(outDir, names.file("cxns")), names.table("cxns"), names.columns("cxns"), header)
                        Cxns.SET.clear()
                        traceDone()

                        traceSaving("corpus")
                        insert(Corpus.SET, Corpus.COMPARATOR, File(outDir, names.file("corpuses")), names.table("corpuses"), names.columns("corpuses"), header)
                        Corpus.SET.clear()
                        traceDone()

                        traceSaving("doc")
                        insert(Doc.SET, Doc.COMPARATOR, File(outDir, names.file("documents")), names.table("documents"), names.columns("documents"), header)
                        Doc.SET.clear()
                        traceDone()

                        Layer.COLLECTOR.open().use {
                            traceSaving("layers")
                            insert(Layer.COLLECTOR, Layer.COLLECTOR, File(outDir, names.file("layers")), names.table("layers"), names.columns("layers"), header, false)
                            traceDone()

                            traceSaving("labels")
                            insertFragmented(Label.SET, Label.COMPARATOR, File(outDir, names.file("labels")), names.table("labels"), names.columns("labels"), header)
                            Label.SET.clear()
                            traceDone()
                        }
                        FeType.COLLECTOR.open().use {
                            traceSaving("fetype")
                            insertStrings(FeType.COLLECTOR, FeType.COLLECTOR, File(outDir, names.file("fetypes")), names.table("fetypes"), names.columns("fetypes"), header)
                            traceDone()

                            traceSaving("lexunit")
                            insert(LexUnit.SET, LexUnit.COMPARATOR, File(outDir, names.file("lexunits")), names.table("lexunits"), names.columns("lexunits"), header)
                            LexUnit.SET.clear()
                            traceDone()

                            traceSaving("lexunit_semtype")
                            insert(LexUnit_SemType.SET, LexUnit_SemType.COMPARATOR, File(outDir, names.file("lexunits_semtypes")), names.table("lexunits_semtypes"), names.columns("lexunits_semtypes"), header)
                            LexUnit_SemType.SET.clear()
                            traceDone()

                            traceSaving("fe_semtype")
                            insert(FE_SemType.SET, FE_SemType.COMPARATOR, File(outDir, names.file("fes_semtypes")), names.table("fes_semtypes"), names.columns("fes_semtypes"), header)
                            FE_SemType.SET.clear()
                            traceDone()
                            Word.COLLECTOR.open().use {
                                traceSaving("lexeme")
                                insertAndIncrement<Lexeme>(Lexeme.SET, Lexeme.COMPARATOR, File(outDir, names.file("lexemes")), names.table("lexemes"), names.columns("lexemes"), header)
                                Lexeme.SET.clear()
                                traceDone()

                                FE.BY_FETYPEID_AND_FRAMEID = makeFEByFETypeIdAndFrameIdMap()
                                try {
                                    traceSaving("fe")
                                    insert(FE.SET, FE.COMPARATOR, File(outDir, names.file("fes")), names.table("fes"), names.columns("fes"), header)
                                    FE.SET.clear()
                                    traceDone()

                                    FERealization.LIST.open().use {
                                        FEGroupRealization.LIST.open().use {
                                            traceSaving("fer")
                                            insert(FERealization.LIST, File(outDir, names.file("ferealizations")), names.table("ferealizations"), names.columns("ferealizations"), header)
                                            traceDone()

                                            traceSaving("fegr")
                                            insert(FEGroupRealization.LIST, File(outDir, names.file("fegrouprealizations")), names.table("fegrouprealizations"), names.columns("fegrouprealizations"), header)
                                            traceDone()

                                            traceSaving("fe_fegr")
                                            insert(
                                                FE_FEGroupRealization.SET,
                                                FE_FEGroupRealization.COMPARATOR,
                                                File(outDir, names.file("fes_fegrouprealizations")),
                                                names.table("fes_fegrouprealizations"),
                                                names.columns("fes_fegrouprealizations"),
                                                header
                                            )
                                            FE_FEGroupRealization.SET.clear()
                                            traceDone()

                                            FEGroupPattern.LIST.open().use {
                                                ValenceUnit.COLLECTOR.open().use {
                                                    traceSaving("fe_fegr")
                                                    insert(
                                                        FEPattern.SET,
                                                        FEPattern.COMPARATOR,
                                                        File(outDir, names.file("ferealizations_valenceunits")),
                                                        names.table("ferealizations_valenceunits"),
                                                        names.columns("ferealizations_valenceunits"),
                                                        header
                                                    )
                                                    FEPattern.SET.clear()
                                                    traceDone()

                                                    traceSaving("valenceunit")
                                                    insert(ValenceUnit.COLLECTOR, ValenceUnit.COLLECTOR, File(outDir, names.file("valenceunits")), names.table("valenceunits"), names.columns("valenceunits"), header)
                                                    traceDone()

                                                    traceSaving("grouppattern")
                                                    insert(FEGroupPattern.LIST, File(outDir, names.file("grouppatterns")), names.table("grouppatterns"), names.columns("grouppatterns"), header)
                                                    traceDone()

                                                    traceSaving("grouppattern_annoset")
                                                    insert(
                                                        FEGroupPattern_AnnoSet.SET,
                                                        null as Comparator<FEGroupPattern_AnnoSet>?,
                                                        File(outDir, names.file("grouppatterns_annosets")),
                                                        names.table("grouppatterns_annosets"),
                                                        names.columns("grouppatterns_annosets"),
                                                        header
                                                    )
                                                    FEGroupPattern_AnnoSet.SET.clear()
                                                    traceDone()

                                                    traceSaving("list", "grouppattern_pattern")
                                                    insert(
                                                        FEGroupPattern_FEPattern.LIST,
                                                        File(outDir, names.file("grouppatterns_patterns")),
                                                        names.table("grouppatterns_patterns"),
                                                        names.columns("grouppatterns_patterns"),
                                                        header
                                                    )
                                                    FEGroupPattern_FEPattern.LIST.clear()
                                                    traceDone()

                                                    traceSaving("valenceunit_annoset")
                                                    insert(
                                                        ValenceUnit_AnnoSet.SET,
                                                        null as Comparator<ValenceUnit_AnnoSet>?,
                                                        File(outDir, names.file("valenceunits_annosets")),
                                                        names.table("valenceunits_annosets"),
                                                        names.columns("valenceunits_annosets"),
                                                        header
                                                    )
                                                    ValenceUnit_AnnoSet.SET.clear()
                                                    traceDone()
                                                }
                                            }
                                        }
                                    }
                                    Governor.COLLECTOR.open().use {
                                        traceSaving("governor")
                                        insert(Governor.COLLECTOR, Governor.COLLECTOR, File(outDir, names.file("governors")), names.table("governors"), names.columns("governors"), header)
                                        traceDone()

                                        traceSaving("lexunit_governor")
                                        insert(LexUnit_Governor.SET, LexUnit_Governor.COMPARATOR, File(outDir, names.file("lexunits_governors")), names.table("lexunits_governors"), names.columns("lexunits_governors"), header)
                                        LexUnit_Governor.SET.clear()
                                        traceDone()

                                        traceSaving("governor_annoset")
                                        insert(Governor_AnnoSet.SET, Governor_AnnoSet.COMPARATOR, File(outDir, names.file("governors_annosets")), names.table("governors_annosets"), names.columns("governors_annosets"), header)
                                        Governor_AnnoSet.SET.clear()
                                        traceDone()
                                    }
                                    traceSaving("sentence")
                                    insert(Sentence.SET, Sentence.COMPARATOR, File(outDir, names.file("sentences")), names.table("sentences"), names.columns("sentences"), header)
                                    Sentence.SET.clear()
                                    traceDone()

                                    SubCorpus.COLLECTOR.open().use {
                                        traceSaving("subcorpus_sentence")
                                        insert(SubCorpus.COLLECTOR, SubCorpus.COLLECTOR, File(outDir, names.file("subcorpuses")), names.table("subcorpuses"), names.columns("subcorpuses"), header)
                                        traceDone()

                                        traceSaving("subcorpus_sentence")
                                        insert(
                                            SubCorpus_Sentence.SET,
                                            SubCorpus_Sentence.COMPARATOR,
                                            File(outDir, names.file("subcorpuses_sentences")),
                                            names.table("subcorpuses_sentences"),
                                            names.columns("subcorpuses_sentences"),
                                            header
                                        )
                                        SubCorpus_Sentence.SET.clear()
                                        traceDone()
                                    }
                                } finally {
                                    FE.BY_FETYPEID_AND_FRAMEID = null
                                }

                                // R E S O L V A B L E

                                insertWords()
                            }
                        }
                    }
                }
            }
        }
    }

    @Throws(FileNotFoundException::class)
    protected open fun insertWords() {
        traceSaving("word")
        insert(Word.COLLECTOR, Word.COLLECTOR, File(outDir, names.file("words")), names.table("words"), names.columns("words"), header)
        traceDone()
    }

    @RequiresIdFrom(type = FeType::class)
    private fun makeFEByFETypeIdAndFrameIdMap(): Map<Pair<Int, Int>, FE> {
        return FE.SET
            .asSequence()
            .map { (getIntId(it.name)!! to it.frameID) to it }
            .toMap()
    }
}
