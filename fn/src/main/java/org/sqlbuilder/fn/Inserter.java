package org.sqlbuilder.fn;

import org.sqlbuilder.common.*;
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
		Insert.insert(SemType.SET, SemType.COMPARATOR, new File(outDir, Names.file("semtypes")), Names.table("semtypes"), Names.columns("semtypes"));
		SemType.SET.clear();
		Progress.traceDone(null);

		Progress.tracePending("set", "semtype_super");
		Insert.insert(SemType_SemTypeSuper.SET, SemType_SemTypeSuper.COMPARATOR, new File(outDir, Names.file("semtypes_supers")), Names.table("semtypes_supers"), Names.columns("semtypes_supers"));
		SemType_SemTypeSuper.SET.clear();
		Progress.traceDone(null);
	}

	public void insertFrames() throws FileNotFoundException
	{
		Progress.tracePending("set", "frame");
		Insert.insert(Frame.SET, Comparator.comparing(Frame::getID), new File(outDir, Names.file("frames")), Names.table("frames"), Names.columns("frames"));
		Frame.SET.clear();
		Progress.traceDone(null);

		try (@ProvidesIdTo(type = FrameRelation.class) var ignored = FrameRelation.COLLECTOR.open())
		{
			Progress.tracePending("collector", "frame_relations");
			Insert.insertStringMap(FrameRelation.COLLECTOR, new File(outDir, Names.file("framerelations")), Names.table("framerelations"), Names.columns("framerelations"));
			Progress.traceDone(null);

			Progress.tracePending("set", "frame_related");
			Insert.insert(Frame_FrameRelated.SET, Frame_FrameRelated.COMPARATOR, new File(outDir, Names.file("frames_related")), Names.table("frames_related"), Names.columns("frames_related"));
			Frame_FrameRelated.SET.clear();
			Progress.traceDone(null);
		}

		Progress.tracePending("set", "fe_required");
		Insert.insert(FE_FERequired.SET, FE_FERequired.COMPARATOR, new File(outDir, Names.file("fes_required")), Names.table("fes_required"), Names.columns("fes_required"));
		FE_FERequired.SET.clear();
		Progress.traceDone(null);

		Progress.tracePending("set", "fe_excluded");
		Insert.insert(FE_FEExcluded.SET, FE_FEExcluded.COMPARATOR, new File(outDir, Names.file("fes_excluded")), Names.table("fes_excluded"), Names.columns("fes_excluded"));
		FE_FEExcluded.SET.clear();
		Progress.traceDone(null);

		Progress.tracePending("set", "frame_semtype");
		Insert.insert(Frame_SemType.SET, Frame_SemType.COMPARATOR, new File(outDir, Names.file("frames_semtypes")), Names.table("frames_semtypes"), Names.columns("frames_semtypes"));
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
			Insert.insert(Values.Pos.MAP, new File(outDir, Names.file("poses")), Names.table("poses"), Names.columns("poses"));
			Insert.insert(Values.CoreType.MAP, new File(outDir, Names.file("coretypes")), Names.table("coretypes"), Names.columns("coretypes"));
			Insert.insert(Values.LabelIType.MAP, new File(outDir, Names.file("labelitypes")), Names.table("labelitypes"), Names.columns("labelitypes"));
			Progress.traceDone(null);

			Progress.tracePending("collector", "gf");
			Insert.insertStringMap(GfType.COLLECTOR, new File(outDir, Names.file("gftypes")), Names.table("gftypes"), Names.columns("gftypes"));
			Progress.traceDone(null);

			Progress.tracePending("collector", "pt");
			Insert.insertStringMap(PtType.COLLECTOR, new File(outDir, Names.file("pttypes")), Names.table("pttypes"), Names.columns("pttypes"));
			Progress.traceDone(null);

			Progress.tracePending("collector", "layertypes");
			Insert.insertStringMap(LayerType.COLLECTOR, new File(outDir, Names.file("layertypes")), Names.table("layertypes"), Names.columns("layertypes"));
			Progress.traceDone(null);

			Progress.tracePending("collector", "labeltypes");
			Insert.insertStringMap(LabelType.COLLECTOR, new File(outDir, Names.file("labeltypes")), Names.table("labeltypes"), Names.columns("labeltypes"));
			Progress.traceDone(null);

			Progress.tracePending("set", "annoset");
			Insert.insert(AnnotationSet.SET, AnnotationSet.COMPARATOR, new File(outDir, Names.file("annosets")), Names.table("annosets"), Names.columns("annosets"));
			AnnotationSet.SET.clear();
			Progress.traceDone(null);

			Progress.tracePending("set", "cxns");
			Insert.insert(Cxns.SET, Cxns.COMPARATOR, new File(outDir, Names.file("cxns")), Names.table("cxns"), Names.columns("cxns"));
			Cxns.SET.clear();
			Progress.traceDone(null);

			Progress.tracePending("set", "corpus");
			Insert.insert(Corpus.SET, Corpus.COMPARATOR, new File(outDir, Names.file("corpuses")), Names.table("corpuses"), Names.columns("corpuses"));
			Corpus.SET.clear();
			Progress.traceDone(null);

			Progress.tracePending("set", "doc");
			Insert.insert(Doc.SET, Doc.COMPARATOR, new File(outDir, Names.file("documents")), Names.table("documents"), Names.columns("documents"));
			Doc.SET.clear();
			Progress.traceDone(null);

			try (@ProvidesIdTo(type = Layer.class) var ignored = Layer.COLLECTOR.open())
			{
				Progress.tracePending("collector", "layer");
				Insert.insert(Layer.COLLECTOR, new File(outDir, Names.file("layers")), Names.table("layers"), Names.columns("layers"), false);
				Progress.traceDone(null);

				Progress.tracePending("set", "label");
				Insert.insertFragmented(Label.SET, Label.COMPARATOR, new File(outDir, Names.file("labels")), Names.table("labels"), Names.columns("labels"));
				Label.SET.clear();
				Progress.traceDone(null);
			}

			try (@ProvidesIdTo(type = FeType.class) var ignored = FeType.COLLECTOR.open())
			{
				Progress.tracePending("collector", "fetype");
				Insert.insertStringMap(FeType.COLLECTOR, new File(outDir, Names.file("fetypes")), Names.table("fetypes"), Names.columns("fetypes"));
				Progress.traceDone(null);

				Progress.tracePending("set", "lexunit");
				Insert.insert(LexUnit.SET, LexUnit.COMPARATOR, new File(outDir, Names.file("lexunits")), Names.table("lexunits"), Names.columns("lexunits"));
				LexUnit.SET.clear();
				Progress.traceDone(null);

				Progress.tracePending("set", "lexunit_semtype");
				Insert.insert(LexUnit_SemType.SET, LexUnit_SemType.COMPARATOR, new File(outDir, Names.file("lexunits_semtypes")), Names.table("lexunits_semtypes"), Names.columns("lexunits_semtypes"));
				LexUnit_SemType.SET.clear();
				Progress.traceDone(null);

				Progress.tracePending("set", "fe_semtype");
				Insert.insert(FE_SemType.SET, FE_SemType.COMPARATOR, new File(outDir, Names.file("fes_semtypes")), Names.table("fes_semtypes"), Names.columns("fes_semtypes"));
				FE_SemType.SET.clear();
				Progress.traceDone(null);

				try (@ProvidesIdTo(type = Word.class) var ignored30 = Word.COLLECTOR.open())
				{
					Progress.tracePending("collector", "word");
					Insert.insert(Word.COLLECTOR, new File(outDir, Names.file("words")), Names.table("words"), Names.columns("words"));
					Progress.traceDone(null);

					Progress.tracePending("set", "lexeme");
					Insert.insertAndIncrement(Lexeme.SET, Lexeme.COMPARATOR, new File(outDir, Names.file("lexemes")), Names.table("lexemes"), Names.columns("lexemes"));
					Lexeme.SET.clear();
					Progress.traceDone(null);

					FE.BY_FETYPEID_AND_FRAMEID = makeFEByFETypeIdAndFrameIdMap();
					try
					{
						Progress.tracePending("set", "fe");
						Insert.insert(FE.SET, FE.COMPARATOR, new File(outDir, Names.file("fes")), Names.table("fes"), Names.columns("fes"));
						FE.SET.clear();
						Progress.traceDone(null);

						try (@ProvidesIdTo(type = FERealization.class) var ignored40 = FERealization.LIST.open(); // vu_fer
						     @ProvidesIdTo(type = FEGroupRealization.class) var ignored41 = FEGroupRealization.LIST.open())
						{
							Progress.tracePending("collector", "fer");
							Insert.insert(FERealization.LIST, new File(outDir, Names.file("ferealizations")), Names.table("ferealizations"), Names.columns("ferealizations"));
							Progress.traceDone(null);

							Progress.tracePending("collector", "fegr");
							Insert.insert(FEGroupRealization.LIST, new File(outDir, Names.file("fegrouprealizations")), Names.table("fegrouprealizations"), Names.columns("fegrouprealizations"));
							Progress.traceDone(null);

							Progress.tracePending("set", "fe_fegr");
							Insert.insert(FE_FEGroupRealization.SET, FE_FEGroupRealization.COMPARATOR, new File(outDir, Names.file("fes_fegrouprealizations")), Names.table("fes_fegrouprealizations"), Names.columns("fes_fegrouprealizations"));
							FE_FEGroupRealization.SET.clear();
							Progress.traceDone(null);

							try (@ProvidesIdTo(type = FEGroupPattern.class) var ignored44 = FEGroupPattern.LIST.open(); //
							     @ProvidesIdTo(type = ValenceUnit.class) var ignored43 = ValenceUnit.COLLECTOR.open())
							{
								Progress.tracePending("set", "fe_fegr");
								Insert.insert(FEPattern.SET, FEPattern.COMPARATOR, new File(outDir, Names.file("ferealizations_valenceunits")), Names.table("ferealizations_valenceunits"), Names.columns("ferealizations_valenceunits"));
								FEPattern.SET.clear();
								Progress.traceDone(null);

								Progress.tracePending("collector", "valenceunit");
								Insert.insert(ValenceUnit.COLLECTOR, new File(outDir, Names.file("valenceunits")), Names.table("valenceunits"), Names.columns("valenceunits"));
								Progress.traceDone(null);

								Progress.tracePending("collector", "grouppattern");
								Insert.insert(FEGroupPattern.LIST, new File(outDir, Names.file("grouppatterns")), Names.table("grouppatterns"), Names.columns("grouppatterns"));
								Progress.traceDone(null);

								Progress.tracePending("collector", "grouppattern_annoset");
								Insert.insert(FEGroupPattern_AnnoSet.SET, null, new File(outDir, Names.file("grouppatterns_annosets")), Names.table("grouppatterns_annosets"), Names.columns("grouppatterns_annosets"));
								FEGroupPattern_AnnoSet.SET.clear();
								Progress.traceDone(null);

								Progress.tracePending("collector", "grouppattern_pattern");
								Insert.insert(FEGroupPattern_FEPattern.SET, null, new File(outDir, Names.file("grouppatterns_patterns")), Names.table("grouppatterns_patterns"), Names.columns("grouppatterns_patterns"));
								FEGroupPattern_FEPattern.SET.clear();
								Progress.traceDone(null);

								Progress.tracePending("collector", "valenceunit_annoset");
								Insert.insert(ValenceUnit_AnnoSet.SET, null, new File(outDir, Names.file("valenceunits_annosets")), Names.table("valenceunits_annosets"), Names.columns("valenceunits_annosets"));
								ValenceUnit_AnnoSet.SET.clear();
								Progress.traceDone(null);
							}
						}

						try (@ProvidesIdTo(type = Governor.class) var ignored42 = Governor.COLLECTOR.open())
						{
							Progress.tracePending("collector", "governor");
							Insert.insert(Governor.COLLECTOR, new File(outDir, Names.file("governors")), Names.table("governors"), Names.columns("governors"));
							Progress.traceDone(null);

							Progress.tracePending("set", "lexunit_governor");
							Insert.insert(LexUnit_Governor.SET, LexUnit_Governor.COMPARATOR, new File(outDir, Names.file("lexunits_governors")), Names.table("lexunits_governors"), Names.columns("lexunits_governors"));
							LexUnit_Governor.SET.clear();
							Progress.traceDone(null);

							Progress.tracePending("set", "governor_annoset");
							Insert.insert(Governor_AnnoSet.SET, Governor_AnnoSet.COMPARATOR, new File(outDir, Names.file("governors_annosets")), Names.table("governors_annosets"), Names.columns("governors_annosets"));
							Governor_AnnoSet.SET.clear();
							Progress.traceDone(null);
						}

						Progress.tracePending("set", "sentence");
						Insert.insert(Sentence.SET, Sentence.COMPARATOR, new File(outDir, Names.file("sentences")), Names.table("sentences"), Names.columns("sentences"));
						Sentence.SET.clear();
						Progress.traceDone(null);

						try (@ProvidesIdTo(type = SubCorpus.class) var ignored50 = SubCorpus.COLLECTOR.open())
						{
							Progress.tracePending("set", "subcorpus_sentence");
							Insert.insert(SubCorpus.COLLECTOR, new File(outDir, Names.file("subcorpuses")), Names.table("subcorpuses"), Names.columns("subcorpuses"));
							Progress.traceDone(null);

							Progress.tracePending("set", "subcorpus_sentence");
							Insert.insert(SubCorpus_Sentence.SET, SubCorpus_Sentence.COMPARATOR, new File(outDir, Names.file("subcorpuses_sentences")), Names.table("subcorpuses_sentences"), Names.columns("subcorpuses_sentences"));
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
