package org.sqlbuilder.fn

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insert
import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Insert.insertAndIncrement
import org.sqlbuilder.common.Insert.insertFragmented
import org.sqlbuilder.common.Insert.insertStrings
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.tracePending
import org.sqlbuilder.fn.joins.*
import org.sqlbuilder.fn.objects.*
import org.sqlbuilder.fn.objects.Values.LabelIType
import org.sqlbuilder.fn.types.*
import org.sqlbuilder.fn.types.FeType.getIntId
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.Throws

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
        tracePending("set", "semtype")
        insert<SemType>(SemType.SET, SemType.COMPARATOR, File(outDir, names.file("semtypes")), names.table("semtypes"), names.columns("semtypes"), header)
        SemType.SET.clear()
        traceDone()

        tracePending("set", "semtype_super")
        insert<SemType_SemTypeSuper>(SemType_SemTypeSuper.SET, SemType_SemTypeSuper.COMPARATOR, File(outDir, names.file("semtypes_supers")), names.table("semtypes_supers"), names.columns("semtypes_supers"), header)
        SemType_SemTypeSuper.SET.clear()
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    fun insertFrames() {
        tracePending("set", "frame")
        insert<Frame>(Frame.SET, Comparator.comparing<Frame, Int?>(Frame::iD), File(outDir, names.file("frames")), names.table("frames"), names.columns("frames"), header)
        Frame.SET.clear()
        traceDone()

        FrameRelation.COLLECTOR.open().use {
            tracePending("collector", "frame_relations")
            insertStrings(FrameRelation.COLLECTOR, FrameRelation.COLLECTOR, File(outDir, names.file("framerelations")), names.table("framerelations"), names.columns("framerelations"), header)
            traceDone()

            tracePending("set", "frame_related")
            insert<Frame_FrameRelated>(Frame_FrameRelated.SET, Frame_FrameRelated.COMPARATOR, File(outDir, names.file("frames_related")), names.table("frames_related"), names.columns("frames_related"), header)
            Frame_FrameRelated.SET.clear()
            traceDone()
        }
        tracePending("set", "fe_required")
        insert<FE_FERequired>(FE_FERequired.SET, FE_FERequired.COMPARATOR, File(outDir, names.file("fes_required")), names.table("fes_required"), names.columns("fes_required"), header)
        FE_FERequired.SET.clear()
        traceDone()

        tracePending("set", "fe_excluded")
        insert<FE_FEExcluded>(FE_FEExcluded.SET, FE_FEExcluded.COMPARATOR, File(outDir, names.file("fes_excluded")), names.table("fes_excluded"), names.columns("fes_excluded"), header)
        FE_FEExcluded.SET.clear()
        traceDone()

        tracePending("set", "frame_semtype")
        insert<Frame_SemType>(Frame_SemType.SET, Frame_SemType.COMPARATOR, File(outDir, names.file("frames_semtypes")), names.table("frames_semtypes"), names.columns("frames_semtypes"), header)
        Frame_SemType.SET.clear()
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    fun insertLexUnitsAndText() {
        GfType.COLLECTOR.open().use {
            PtType.COLLECTOR.open().use {
                LayerType.COLLECTOR.open().use {
                    LabelType.COLLECTOR.open().use {

                        tracePending("maps", "pos,coretype,labelitype")
                        Insert.insert<Values.Pos>(Values.Pos.MAP.keys, { Values.Pos.MAP[it]!! }, File(outDir, names.file("poses")), names.table("poses"), names.columns("poses"), header)
                        Insert.insert<Values.CoreType>(Values.CoreType.MAP.keys, { Values.CoreType.MAP[it]!! }, File(outDir, names.file("coretypes")), names.table("coretypes"), names.columns("coretypes"), header)
                        Insert.insert<LabelIType>(LabelIType.MAP.keys, { LabelIType.MAP[it]!! }, File(outDir, names.file("labelitypes")), names.table("labelitypes"), names.columns("labelitypes"), header)
                        traceDone()

                        tracePending("collector", "gf")
                        insertStrings(GfType.COLLECTOR, GfType.COLLECTOR, File(outDir, names.file("gftypes")), names.table("gftypes"), names.columns("gftypes"), header)
                        traceDone()

                        tracePending("collector", "pt")
                        insertStrings(PtType.COLLECTOR, PtType.COLLECTOR, File(outDir, names.file("pttypes")), names.table("pttypes"), names.columns("pttypes"), header)
                        traceDone()

                        tracePending("collector", "layertypes")
                        insertStrings(LayerType.COLLECTOR, LayerType.COLLECTOR, File(outDir, names.file("layertypes")), names.table("layertypes"), names.columns("layertypes"), header)
                        traceDone()

                        tracePending("collector", "labeltypes")
                        insertStrings(LabelType.COLLECTOR, LabelType.COLLECTOR, File(outDir, names.file("labeltypes")), names.table("labeltypes"), names.columns("labeltypes"), header)
                        traceDone()

                        tracePending("set", "annoset")
                        insert<AnnotationSet>(AnnotationSet.SET, AnnotationSet.COMPARATOR, File(outDir, names.file("annosets")), names.table("annosets"), names.columns("annosets"), header)
                        AnnotationSet.SET.clear()
                        traceDone()

                        tracePending("set", "cxns")
                        insert<Cxns>(Cxns.SET, Cxns.COMPARATOR, File(outDir, names.file("cxns")), names.table("cxns"), names.columns("cxns"), header)
                        Cxns.SET.clear()
                        traceDone()

                        tracePending("set", "corpus")
                        insert<Corpus>(Corpus.SET, Corpus.COMPARATOR, File(outDir, names.file("corpuses")), names.table("corpuses"), names.columns("corpuses"), header)
                        Corpus.SET.clear()
                        traceDone()

                        tracePending("set", "doc")
                        insert<Doc>(Doc.SET, Doc.COMPARATOR, File(outDir, names.file("documents")), names.table("documents"), names.columns("documents"), header)
                        Doc.SET.clear()
                        traceDone()

                        Layer.COLLECTOR.open().use {
                            tracePending("collector", "layer")
                            insert<Layer>(Layer.COLLECTOR, Layer.COLLECTOR, File(outDir, names.file("layers")), names.table("layers"), names.columns("layers"), header, false)
                            traceDone()

                            tracePending("set", "label")
                            insertFragmented<Label>(Label.SET, Label.COMPARATOR, File(outDir, names.file("labels")), names.table("labels"), names.columns("labels"), header)
                            Label.SET.clear()
                            traceDone()
                        }
                        FeType.COLLECTOR.open().use {
                            tracePending("collector", "fetype")
                            insertStrings(FeType.COLLECTOR, FeType.COLLECTOR, File(outDir, names.file("fetypes")), names.table("fetypes"), names.columns("fetypes"), header)
                            traceDone()

                            tracePending("set", "lexunit")
                            insert<LexUnit>(LexUnit.SET, LexUnit.COMPARATOR, File(outDir, names.file("lexunits")), names.table("lexunits"), names.columns("lexunits"), header)
                            LexUnit.SET.clear()
                            traceDone()

                            tracePending("set", "lexunit_semtype")
                            insert<LexUnit_SemType>(LexUnit_SemType.SET, LexUnit_SemType.COMPARATOR, File(outDir, names.file("lexunits_semtypes")), names.table("lexunits_semtypes"), names.columns("lexunits_semtypes"), header)
                            LexUnit_SemType.SET.clear()
                            traceDone()

                            tracePending("set", "fe_semtype")
                            insert<FE_SemType>(FE_SemType.SET, FE_SemType.COMPARATOR, File(outDir, names.file("fes_semtypes")), names.table("fes_semtypes"), names.columns("fes_semtypes"), header)
                            FE_SemType.SET.clear()
                            traceDone()
                            Word.COLLECTOR.open().use {
                                tracePending("set", "lexeme")
                                insertAndIncrement<Lexeme>(Lexeme.SET, Lexeme.COMPARATOR, File(outDir, names.file("lexemes")), names.table("lexemes"), names.columns("lexemes"), header)
                                Lexeme.SET.clear()
                                traceDone()

                                FE.BY_FETYPEID_AND_FRAMEID = makeFEByFETypeIdAndFrameIdMap()
                                try {
                                    tracePending("set", "fe")
                                    insert<FE>(FE.SET, FE.COMPARATOR, File(outDir, names.file("fes")), names.table("fes"), names.columns("fes"), header)
                                    FE.SET.clear()
                                    traceDone()

                                    FERealization.LIST.open().use {
                                        FEGroupRealization.LIST.open().use {
                                            tracePending("collector", "fer")
                                            insert<FERealization>(FERealization.LIST, File(outDir, names.file("ferealizations")), names.table("ferealizations"), names.columns("ferealizations"), header)
                                            traceDone()

                                            tracePending("collector", "fegr")
                                            insert<FEGroupRealization>(FEGroupRealization.LIST, File(outDir, names.file("fegrouprealizations")), names.table("fegrouprealizations"), names.columns("fegrouprealizations"), header)
                                            traceDone()

                                            tracePending("set", "fe_fegr")
                                            insert<FE_FEGroupRealization>(
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
                                                    tracePending("set", "fe_fegr")
                                                    insert<FEPattern>(
                                                        FEPattern.SET,
                                                        FEPattern.COMPARATOR,
                                                        File(outDir, names.file("ferealizations_valenceunits")),
                                                        names.table("ferealizations_valenceunits"),
                                                        names.columns("ferealizations_valenceunits"),
                                                        header
                                                    )
                                                    FEPattern.SET.clear()
                                                    traceDone()

                                                    tracePending("collector", "valenceunit")
                                                    Insert.insert<ValenceUnit>(ValenceUnit.COLLECTOR, ValenceUnit.COLLECTOR, File(outDir, names.file("valenceunits")), names.table("valenceunits"), names.columns("valenceunits"), header)
                                                    traceDone()

                                                    tracePending("collector", "grouppattern")
                                                    insert<FEGroupPattern>(FEGroupPattern.LIST, File(outDir, names.file("grouppatterns")), names.table("grouppatterns"), names.columns("grouppatterns"), header)
                                                    traceDone()

                                                    tracePending("collector", "grouppattern_annoset")
                                                    insert<FEGroupPattern_AnnoSet>(
                                                        FEGroupPattern_AnnoSet.SET,
                                                        null as Comparator<FEGroupPattern_AnnoSet>?,
                                                        File(outDir, names.file("grouppatterns_annosets")),
                                                        names.table("grouppatterns_annosets"),
                                                        names.columns("grouppatterns_annosets"),
                                                        header
                                                    )
                                                    FEGroupPattern_AnnoSet.SET.clear()
                                                    traceDone()

                                                    tracePending("list", "grouppattern_pattern")
                                                    insert<FEGroupPattern_FEPattern>(
                                                        FEGroupPattern_FEPattern.LIST,
                                                        File(outDir, names.file("grouppatterns_patterns")),
                                                        names.table("grouppatterns_patterns"),
                                                        names.columns("grouppatterns_patterns"),
                                                        header
                                                    )
                                                    FEGroupPattern_FEPattern.LIST.clear()
                                                    traceDone()

                                                    tracePending("collector", "valenceunit_annoset")
                                                    insert<ValenceUnit_AnnoSet>(
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
                                        tracePending("collector", "governor")
                                        Insert.insert<Governor>(Governor.COLLECTOR, Governor.COLLECTOR, File(outDir, names.file("governors")), names.table("governors"), names.columns("governors"), header)
                                        traceDone()

                                        tracePending("set", "lexunit_governor")
                                        insert<LexUnit_Governor>(LexUnit_Governor.SET, LexUnit_Governor.COMPARATOR, File(outDir, names.file("lexunits_governors")), names.table("lexunits_governors"), names.columns("lexunits_governors"), header)
                                        LexUnit_Governor.SET.clear()
                                        traceDone()

                                        tracePending("set", "governor_annoset")
                                        insert<Governor_AnnoSet>(Governor_AnnoSet.SET, Governor_AnnoSet.COMPARATOR, File(outDir, names.file("governors_annosets")), names.table("governors_annosets"), names.columns("governors_annosets"), header)
                                        Governor_AnnoSet.SET.clear()
                                        traceDone()
                                    }
                                    tracePending("set", "sentence")
                                    insert<Sentence>(Sentence.SET, Sentence.COMPARATOR, File(outDir, names.file("sentences")), names.table("sentences"), names.columns("sentences"), header)
                                    Sentence.SET.clear()
                                    traceDone()

                                    SubCorpus.COLLECTOR.open().use {
                                        tracePending("set", "subcorpus_sentence")
                                        Insert.insert<SubCorpus>(SubCorpus.COLLECTOR, SubCorpus.COLLECTOR, File(outDir, names.file("subcorpuses")), names.table("subcorpuses"), names.columns("subcorpuses"), header)
                                        traceDone()

                                        tracePending("set", "subcorpus_sentence")
                                        insert<SubCorpus_Sentence>(
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
        tracePending("collector", "word")
        Insert.insert<Word>(Word.COLLECTOR, Word.COLLECTOR, File(outDir, names.file("words")), names.table("words"), names.columns("words"), header)
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
