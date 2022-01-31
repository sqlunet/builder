package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.fn.joins.*;
import org.sqlbuilder.fn.objects.*;
import org.sqlbuilder.fn.types.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;

public class Inserter
{
	private final File outDir;

	public Inserter(final Properties conf)
	{
		this.outDir = new File(conf.getProperty("fnoutdir", "sql/data"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}
	}

	public void insertPreset() throws FileNotFoundException
	{
	}

	public void insertSemTypes() throws FileNotFoundException
	{
		Progress.tracePending("set", "semtype");
		Insert.insert(SemType.SET, SemType.COMPARATOR, new File(outDir, Names.SEMTYPES.FILE), Names.SEMTYPES.TABLE, Names.SEMTYPES.COLUMNS);
		SemType.SET.clear();
		Progress.traceDone(null);

		Progress.tracePending("set", "semtype_super");
		Insert.insert(SemType_SemTypeSuper.SET, SemType_SemTypeSuper.COMPARATOR, new File(outDir, Names.SEMTYPES_SUPERS.FILE), Names.SEMTYPES_SUPERS.TABLE, Names.SEMTYPES_SUPERS.COLUMNS);
		SemType_SemTypeSuper.SET.clear();
		Progress.traceDone(null);
	}

	public void insertFrames() throws FileNotFoundException
	{
		Progress.tracePending("set", "frame");
		Insert.insert(Frame.SET, Comparator.comparing(Frame::getID), new File(outDir, Names.FRAMES.FILE), Names.FRAMES.TABLE, Names.FRAMES.COLUMNS);
		Frame.SET.clear();
		Progress.traceDone(null);

		try (@ProvidesIdTo(type = FrameRelation.class) var ignored = FrameRelation.COLLECTOR.open())
		{
			Progress.tracePending("collector", "frame_relations");
			Insert.insertStringMap(FrameRelation.COLLECTOR, new File(outDir, Names.FRAMERELATIONS.FILE), Names.FRAMERELATIONS.TABLE, Names.FRAMERELATIONS.COLUMNS);
			Progress.traceDone(null);

			Progress.tracePending("set", "frame_related");
			Insert.insert(Frame_FrameRelated.SET, Frame_FrameRelated.COMPARATOR, new File(outDir, Names.FRAMES_RELATED.FILE), Names.FRAMES_RELATED.TABLE, Names.FRAMES_RELATED.COLUMNS);
			Frame_FrameRelated.SET.clear();
			Progress.traceDone(null);
		}

		Progress.tracePending("set", "fe_required");
		Insert.insert(FE_FERequired.SET, FE_FERequired.COMPARATOR, new File(outDir, Names.FES_REQUIRED.FILE), Names.FES_REQUIRED.TABLE, Names.FES_REQUIRED.COLUMNS);
		FE_FERequired.SET.clear();
		Progress.traceDone(null);

		Progress.tracePending("set", "fe_excluded");
		Insert.insert(FE_FEExcluded.SET, FE_FEExcluded.COMPARATOR, new File(outDir, Names.FES_EXCLUDED.FILE), Names.FES_EXCLUDED.TABLE, Names.FES_EXCLUDED.COLUMNS);
		FE_FEExcluded.SET.clear();
		Progress.traceDone(null);

		Progress.tracePending("set", "frame_semtype");
		Insert.insert(Frame_SemType.SET, Frame_SemType.COMPARATOR, new File(outDir, Names.FRAMES_SEMTYPES.FILE), Names.FRAMES_SEMTYPES.TABLE, Names.FRAMES_SEMTYPES.COLUMNS);
		Frame_SemType.SET.clear();
		Progress.traceDone(null);
	}

	public void insertLexUnits() throws FileNotFoundException
	{
	}

	public void insertFullText() throws FileNotFoundException
	{
	}

	public void insertFinal() throws FileNotFoundException
	{
		try (
		     @ProvidesIdTo(type = GfType.class) var ignored11 = GfType.COLLECTOR.open(); //
		     @ProvidesIdTo(type = PtType.class) var ignored12 = PtType.COLLECTOR.open(); //
		     @ProvidesIdTo(type = LayerType.class) var ignored13 = LayerType.COLLECTOR.open(); //
		     @ProvidesIdTo(type = LabelType.class) var ignored14 = LabelType.COLLECTOR.open())
		{
			Progress.tracePending("collectors", "pos,coretype,labelitype");
			Insert.insert(Values.Pos.MAP, new File(outDir, Names.POSES.FILE), Names.POSES.TABLE, Names.POSES.COLUMNS);
			Insert.insert(Values.CoreType.MAP, new File(outDir, Names.CORETYPES.FILE), Names.CORETYPES.TABLE, Names.CORETYPES.COLUMNS);
			Insert.insert(Values.LabelIType.MAP, new File(outDir, Names.LABELITYPES.FILE), Names.LABELITYPES.TABLE, Names.LABELITYPES.COLUMNS);
			Progress.traceDone(null);

			Progress.tracePending("collector", "gf");
			Insert.insertStringMap(GfType.COLLECTOR, new File(outDir, Names.GFTYPES.FILE), Names.GFTYPES.TABLE, Names.GFTYPES.COLUMNS);
			Progress.traceDone(null);

			Progress.tracePending("collector", "pt");
			Insert.insertStringMap(PtType.COLLECTOR, new File(outDir, Names.PTTYPES.FILE), Names.PTTYPES.TABLE, Names.PTTYPES.COLUMNS);
			Progress.traceDone(null);

			Progress.tracePending("collector", "layertypes");
			Insert.insertStringMap(LayerType.COLLECTOR, new File(outDir, Names.LAYERTYPES.FILE), Names.LAYERTYPES.TABLE, Names.LAYERTYPES.COLUMNS);
			Progress.traceDone(null);

			Progress.tracePending("collector", "labeltypes");
			Insert.insertStringMap(LabelType.COLLECTOR, new File(outDir, Names.LABELTYPES.FILE), Names.LABELTYPES.TABLE, Names.LABELTYPES.COLUMNS);
			Progress.traceDone(null);

			Progress.tracePending("set", "annoset");
			Insert.insert(AnnotationSet.SET, AnnotationSet.COMPARATOR, new File(outDir, Names.ANNOSETS.FILE), Names.ANNOSETS.TABLE, Names.ANNOSETS.COLUMNS);
			AnnotationSet.SET.clear();
			Progress.traceDone(null);

			Progress.tracePending("set", "cxns");
			Insert.insert(Cxns.SET, Cxns.COMPARATOR, new File(outDir, Names.CXNS.FILE), Names.CXNS.TABLE, Names.CXNS.COLUMNS);
			Cxns.SET.clear();
			Progress.traceDone(null);

			Progress.tracePending("set", "corpus");
			Insert.insert(Corpus.SET, Corpus.COMPARATOR, new File(outDir, Names.CORPUSES.FILE), Names.CORPUSES.TABLE, Names.CORPUSES.COLUMNS);
			Corpus.SET.clear();
			Progress.traceDone(null);

			Progress.tracePending("set", "doc");
			Insert.insert(Doc.SET, Doc.COMPARATOR, new File(outDir, Names.DOCUMENTS.FILE), Names.DOCUMENTS.TABLE, Names.DOCUMENTS.COLUMNS);
			Doc.SET.clear();
			Progress.traceDone(null);

			try (@ProvidesIdTo(type = Layer.class) var ignored = Layer.COLLECTOR.open())
			{
				Progress.tracePending("collector", "layer");
				Insert.insertNoNumber(Layer.COLLECTOR, new File(outDir, Names.LAYERS.FILE), Names.LAYERS.TABLE, Names.LAYERS.COLUMNS);
				Progress.traceDone(null);

				Progress.tracePending("set", "label");
				Insert.insert(Label.SET, Label.COMPARATOR, new File(outDir, Names.LABELS.FILE), Names.LABELS.TABLE, Names.LABELS.COLUMNS);
				Label.SET.clear();
				Progress.traceDone(null);
			}

			try (@ProvidesIdTo(type = FeType.class) var ignored = FeType.COLLECTOR.open())
			{
				Progress.tracePending("collector", "fetype");
				Insert.insertStringMap(FeType.COLLECTOR, new File(outDir, Names.FETYPES.FILE), Names.FETYPES.TABLE, Names.FETYPES.COLUMNS);
				Progress.traceDone(null);

				Progress.tracePending("set", "lexunit");
				Insert.insert(LexUnit.SET, LexUnit.COMPARATOR, new File(outDir, Names.LEXUNITS.FILE), Names.LEXUNITS.TABLE, Names.LEXUNITS.COLUMNS);
				LexUnit.SET.clear();
				Progress.traceDone(null);

				Progress.tracePending("set", "lexunit_semtype");
				Insert.insert(LexUnit_SemType.SET, LexUnit_SemType.COMPARATOR, new File(outDir, Names.LEXUNITS_SEMTYPES.FILE), Names.LEXUNITS_SEMTYPES.TABLE, Names.LEXUNITS_SEMTYPES.COLUMNS);
				LexUnit_SemType.SET.clear();
				Progress.traceDone(null);

				Progress.tracePending("set", "fe_semtype");
				Insert.insert(FE_SemType.SET, FE_SemType.COMPARATOR, new File(outDir, Names.FES_SEMTYPES.FILE), Names.FES_SEMTYPES.TABLE, Names.FES_SEMTYPES.COLUMNS);
				FE_SemType.SET.clear();
				Progress.traceDone(null);

				try (@ProvidesIdTo(type = Word.class) var ignored30 = Word.COLLECTOR.open())
				{
					Progress.tracePending("collector", "word");
					Insert.insert(Word.COLLECTOR, new File(outDir, Names.WORDS.FILE), Names.WORDS.TABLE, Names.WORDS.COLUMNS);
					Progress.traceDone(null);

					Progress.tracePending("set", "lexeme");
					Insert.insertAndIncrement(Lexeme.SET, Lexeme.COMPARATOR, new File(outDir, Names.LEXEMES.FILE), Names.LEXEMES.TABLE, Names.LEXEMES.COLUMNS);
					Lexeme.SET.clear();
					Progress.traceDone(null);

					FE.BY_FETYPEID_AND_FRAMEID = makeFEByFETypeIdAndFrameIdMap();
					try
					{
						Progress.tracePending("set", "fe");
						Insert.insert(FE.SET, FE.COMPARATOR, new File(outDir, Names.FES.FILE), Names.FES.TABLE, Names.FES.COLUMNS);
						FE.SET.clear();
						Progress.traceDone(null);

						try (@ProvidesIdTo(type = FERealization.class) var ignored40 = FERealization.LIST.open(); // vu_fer
						     @ProvidesIdTo(type = FEGroupRealization.class) var ignored41 = FEGroupRealization.LIST.open())
						{
							Progress.tracePending("collector", "fer");
							Insert.insert(FERealization.LIST, new File(outDir, Names.FEREALIZATIONS.FILE), Names.FEREALIZATIONS.TABLE, Names.FEREALIZATIONS.COLUMNS);
							Progress.traceDone(null);

							Progress.tracePending("collector", "fegr");
							Insert.insert(FEGroupRealization.LIST, new File(outDir, Names.FEGROUPREALIZATIONS.FILE), Names.FEGROUPREALIZATIONS.TABLE, Names.FEGROUPREALIZATIONS.COLUMNS);
							Progress.traceDone(null);

							Progress.tracePending("set", "fe_fegr");
							Insert.insert(FE_FEGroupRealization.SET, FE_FEGroupRealization.COMPARATOR, new File(outDir, Names.FES_FEGROUPREALIZATIONS.FILE), Names.FES_FEGROUPREALIZATIONS.TABLE, Names.FES_FEGROUPREALIZATIONS.COLUMNS);
							FE_FEGroupRealization.SET.clear();
							Progress.traceDone(null);

							try (@ProvidesIdTo(type = FEGroupPattern.class) var ignored44 = FEGroupPattern.COLLECTOR.open(); //
							     @ProvidesIdTo(type = ValenceUnit.class) var ignored43 = ValenceUnit.COLLECTOR.open())
							{
								Progress.tracePending("set", "fe_fegr");
								Insert.insert(FEPattern.SET, FEPattern.COMPARATOR, new File(outDir, Names.FEREALIZATIONS_VALENCEUNITS.FILE), Names.FEREALIZATIONS_VALENCEUNITS.TABLE, Names.FEREALIZATIONS_VALENCEUNITS.COLUMNS);
								FEPattern.SET.clear();
								Progress.traceDone(null);

								Progress.tracePending("collector", "valenceunit");
								Insert.insert(ValenceUnit.COLLECTOR, new File(outDir, Names.VALENCEUNITS.FILE), Names.VALENCEUNITS.TABLE, Names.VALENCEUNITS.COLUMNS);
								Progress.traceDone(null);

								Progress.tracePending("collector", "pattern");
								Insert.insert(FEGroupPattern.COLLECTOR, new File(outDir, Names.GROUPPATTERNS.FILE), Names.GROUPPATTERNS.TABLE, Names.GROUPPATTERNS.COLUMNS);
								Progress.traceDone(null);

								Progress.tracePending("collector", "pattern_annoset");
								Insert.insert(FEGroupPattern_AnnoSet.SET, null, new File(outDir, Names.GROUPPATTERNS_ANNOSETS.FILE), Names.GROUPPATTERNS_ANNOSETS.TABLE, Names.GROUPPATTERNS_ANNOSETS.COLUMNS);
								FEGroupPattern_AnnoSet.SET.clear();
								Progress.traceDone(null);

								Progress.tracePending("collector", "grouppattern_patterns");
								Insert.insert(FEGroupPattern_FEPattern.SET, null, new File(outDir, Names.GROUPPATTERNS_PATTERNS.FILE), Names.GROUPPATTERNS_PATTERNS.TABLE, Names.GROUPPATTERNS_PATTERNS.COLUMNS);
								FEGroupPattern_FEPattern.SET.clear();
								Progress.traceDone(null);

								Progress.tracePending("collector", "valenceunit_annoset");
								Insert.insert(ValenceUnit_AnnoSet.SET, null, new File(outDir, Names.VALENCEUNITS_ANNOSETS.FILE), Names.VALENCEUNITS_ANNOSETS.TABLE, Names.VALENCEUNITS_ANNOSETS.COLUMNS);
								ValenceUnit_AnnoSet.SET.clear();
								Progress.traceDone(null);
							}
						}

						try (@ProvidesIdTo(type = Governor.class) var ignored42 = Governor.COLLECTOR.open())
						{
							Progress.tracePending("collector", "governor");
							Insert.insert(Governor.COLLECTOR, new File(outDir, Names.GOVERNORS.FILE), Names.GOVERNORS.TABLE, Names.GOVERNORS.COLUMNS);
							Progress.traceDone(null);

							Progress.tracePending("set", "lexunit_governor");
							Insert.insert(LexUnit_Governor.SET, LexUnit_Governor.COMPARATOR, new File(outDir, Names.LEXUNITS_GOVERNORS.FILE), Names.LEXUNITS_GOVERNORS.TABLE, Names.LEXUNITS_GOVERNORS.COLUMNS);
							LexUnit_Governor.SET.clear();
							Progress.traceDone(null);

							Progress.tracePending("set", "governor_annoset");
							Insert.insert(Governor_AnnoSet.SET, Governor_AnnoSet.COMPARATOR, new File(outDir, Names.GOVERNORS_ANNOSETS.FILE), Names.GOVERNORS_ANNOSETS.TABLE, Names.GOVERNORS_ANNOSETS.COLUMNS);
							Governor_AnnoSet.SET.clear();
							Progress.traceDone(null);
						}

						Progress.tracePending("set", "sentence");
						Insert.insert(Sentence.SET, Sentence.COMPARATOR, new File(outDir, Names.SENTENCES.FILE), Names.SENTENCES.TABLE, Names.SENTENCES.COLUMNS);
						Sentence.SET.clear();
						Progress.traceDone(null);

						try (@ProvidesIdTo(type = SubCorpus.class) var ignored50 = SubCorpus.COLLECTOR.open())
						{
							Progress.tracePending("set", "subcorpus_sentence");
							Insert.insert(SubCorpus.COLLECTOR, new File(outDir, Names.SUBCORPUSES.FILE), Names.SUBCORPUSES.TABLE, Names.SUBCORPUSES.COLUMNS);
							Progress.traceDone(null);

							Progress.tracePending("set", "subcorpus_sentence");
							Insert.insert(SubCorpus_Sentence.SET, SubCorpus_Sentence.COMPARATOR, new File(outDir, Names.SUBCORPUSES_SENTENCES.FILE), Names.SUBCORPUSES_SENTENCES.TABLE, Names.SUBCORPUSES_SENTENCES.COLUMNS);
							SubCorpus_Sentence.SET.clear();
							Progress.traceDone(null);
						}
					}
					finally
					{
						FE.BY_FETYPEID_AND_FRAMEID = null;
					}
				}
			}
		}
	}

	@RequiresIdFrom(type=FeType.class)
	private Map<Pair<Integer, Integer>, FE> makeFEByFETypeIdAndFrameIdMap()
	{
		return FE.SET.stream() //
				.map(fe -> new SimpleEntry<>(new Pair<>(FeType.getIntId(fe.getName()), fe.getFrameID()), fe)) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
	}
}
