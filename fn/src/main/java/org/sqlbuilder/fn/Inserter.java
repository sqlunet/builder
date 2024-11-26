package org.sqlbuilder.fn;

import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Names;
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
	protected final Names names;

	protected String header;

	protected File outDir;

	public Inserter(final Properties conf)
	{
		this.names = new Names("fn");
		this.header = conf.getProperty("fn_header");
		this.outDir = new File(conf.getProperty("fn_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	public void insert() throws FileNotFoundException
	{
		insertSemTypes();
		insertFrames();
		insertLexUnitsAndText();
	}

	public void insertSemTypes() throws FileNotFoundException
	{
		Progress.tracePending("set", "semtype");
		Insert.insert2(SemType.SET, SemType.COMPARATOR, new File(outDir, names.file("semtypes")), names.table("semtypes"), names.columns("semtypes"), header);
		SemType.SET.clear();
		Progress.traceDone();

		Progress.tracePending("set", "semtype_super");
		Insert.insert2(SemType_SemTypeSuper.SET, SemType_SemTypeSuper.COMPARATOR, new File(outDir, names.file("semtypes_supers")), names.table("semtypes_supers"), names.columns("semtypes_supers"), header);
		SemType_SemTypeSuper.SET.clear();
		Progress.traceDone();
	}

	public void insertFrames() throws FileNotFoundException
	{
		Progress.tracePending("set", "frame");
		Insert.insert2(Frame.SET, Comparator.comparing(Frame::getID), new File(outDir, names.file("frames")), names.table("frames"), names.columns("frames"), header);
		Frame.SET.clear();
		Progress.traceDone();

		try (@ProvidesIdTo(type = FrameRelation.class) var ignored = FrameRelation.COLLECTOR.open())
		{
			Progress.tracePending("collector", "frame_relations");
			Insert.insertStringMap2(FrameRelation.COLLECTOR, FrameRelation.COLLECTOR, new File(outDir, names.file("framerelations")), names.table("framerelations"), names.columns("framerelations"), header);
			Progress.traceDone();

			Progress.tracePending("set", "frame_related");
			Insert.insert2(Frame_FrameRelated.SET, Frame_FrameRelated.COMPARATOR, new File(outDir, names.file("frames_related")), names.table("frames_related"), names.columns("frames_related"), header);
			Frame_FrameRelated.SET.clear();
			Progress.traceDone();
		}

		Progress.tracePending("set", "fe_required");
		Insert.insert2(FE_FERequired.SET, FE_FERequired.COMPARATOR, new File(outDir, names.file("fes_required")), names.table("fes_required"), names.columns("fes_required"), header);
		FE_FERequired.SET.clear();
		Progress.traceDone();

		Progress.tracePending("set", "fe_excluded");
		Insert.insert2(FE_FEExcluded.SET, FE_FEExcluded.COMPARATOR, new File(outDir, names.file("fes_excluded")), names.table("fes_excluded"), names.columns("fes_excluded"), header);
		FE_FEExcluded.SET.clear();
		Progress.traceDone();

		Progress.tracePending("set", "frame_semtype");
		Insert.insert2(Frame_SemType.SET, Frame_SemType.COMPARATOR, new File(outDir, names.file("frames_semtypes")), names.table("frames_semtypes"), names.columns("frames_semtypes"), header);
		Frame_SemType.SET.clear();
		Progress.traceDone();
	}

	public void insertLexUnitsAndText() throws FileNotFoundException
	{
		try (@ProvidesIdTo(type = GfType.class) var ignored11 = GfType.COLLECTOR.open(); //
		     @ProvidesIdTo(type = PtType.class) var ignored12 = PtType.COLLECTOR.open(); //
		     @ProvidesIdTo(type = LayerType.class) var ignored13 = LayerType.COLLECTOR.open(); //
		     @ProvidesIdTo(type = LabelType.class) var ignored14 = LabelType.COLLECTOR.open())
		{
			Progress.tracePending("maps", "pos,coretype,labelitype");
			Insert.insert2(Values.Pos.MAP.keySet(), Values.Pos.MAP::get,new File(outDir, names.file("poses")), names.table("poses"), names.columns("poses"), header);
			Insert.insert2(Values.CoreType.MAP.keySet(), Values.CoreType.MAP::get, new File(outDir, names.file("coretypes")), names.table("coretypes"), names.columns("coretypes"), header);
			Insert.insert2(Values.LabelIType.MAP.keySet(), Values.LabelIType.MAP::get, new File(outDir, names.file("labelitypes")), names.table("labelitypes"), names.columns("labelitypes"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "gf");
			Insert.insertStringMap2(GfType.COLLECTOR, GfType.COLLECTOR, new File(outDir, names.file("gftypes")), names.table("gftypes"), names.columns("gftypes"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "pt");
			Insert.insertStringMap2(PtType.COLLECTOR, PtType.COLLECTOR, new File(outDir, names.file("pttypes")), names.table("pttypes"), names.columns("pttypes"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "layertypes");
			Insert.insertStringMap2(LayerType.COLLECTOR, LayerType.COLLECTOR, new File(outDir, names.file("layertypes")), names.table("layertypes"), names.columns("layertypes"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "labeltypes");
			Insert.insertStringMap2(LabelType.COLLECTOR, LabelType.COLLECTOR, new File(outDir, names.file("labeltypes")), names.table("labeltypes"), names.columns("labeltypes"), header);
			Progress.traceDone();

			Progress.tracePending("set", "annoset");
			Insert.insert2(AnnotationSet.SET, AnnotationSet.COMPARATOR, new File(outDir, names.file("annosets")), names.table("annosets"), names.columns("annosets"), header);
			AnnotationSet.SET.clear();
			Progress.traceDone();

			Progress.tracePending("set", "cxns");
			Insert.insert2(Cxns.SET, Cxns.COMPARATOR, new File(outDir, names.file("cxns")), names.table("cxns"), names.columns("cxns"), header);
			Cxns.SET.clear();
			Progress.traceDone();

			Progress.tracePending("set", "corpus");
			Insert.insert2(Corpus.SET, Corpus.COMPARATOR, new File(outDir, names.file("corpuses")), names.table("corpuses"), names.columns("corpuses"), header);
			Corpus.SET.clear();
			Progress.traceDone();

			Progress.tracePending("set", "doc");
			Insert.insert2(Doc.SET, Doc.COMPARATOR, new File(outDir, names.file("documents")), names.table("documents"), names.columns("documents"), header);
			Doc.SET.clear();
			Progress.traceDone();

			try (@ProvidesIdTo(type = Layer.class) var ignored20 = Layer.COLLECTOR.open())
			{
				Progress.tracePending("collector", "layer");
				Insert.insert2(Layer.COLLECTOR, Layer.COLLECTOR, new File(outDir, names.file("layers")), names.table("layers"), names.columns("layers"), header, false);
				Progress.traceDone();

				Progress.tracePending("set", "label");
				Insert.insertFragmented2(Label.SET, Label.COMPARATOR, new File(outDir, names.file("labels")), names.table("labels"), names.columns("labels"), header);
				Label.SET.clear();
				Progress.traceDone();
			}

			try (@ProvidesIdTo(type = FeType.class) var ignored21 = FeType.COLLECTOR.open())
			{
				Progress.tracePending("collector", "fetype");
				Insert.insertStringMap2(FeType.COLLECTOR, FeType.COLLECTOR, new File(outDir, names.file("fetypes")), names.table("fetypes"), names.columns("fetypes"), header);
				Progress.traceDone();

				Progress.tracePending("set", "lexunit");
				Insert.insert2(LexUnit.SET, LexUnit.COMPARATOR, new File(outDir, names.file("lexunits")), names.table("lexunits"), names.columns("lexunits"), header);
				LexUnit.SET.clear();
				Progress.traceDone();

				Progress.tracePending("set", "lexunit_semtype");
				Insert.insert2(LexUnit_SemType.SET, LexUnit_SemType.COMPARATOR, new File(outDir, names.file("lexunits_semtypes")), names.table("lexunits_semtypes"), names.columns("lexunits_semtypes"), header);
				LexUnit_SemType.SET.clear();
				Progress.traceDone();

				Progress.tracePending("set", "fe_semtype");
				Insert.insert2(FE_SemType.SET, FE_SemType.COMPARATOR, new File(outDir, names.file("fes_semtypes")), names.table("fes_semtypes"), names.columns("fes_semtypes"), header);
				FE_SemType.SET.clear();
				Progress.traceDone();

				try (@ProvidesIdTo(type = Word.class) var ignored30 = Word.COLLECTOR.open())
				{
					Progress.tracePending("set", "lexeme");
					Insert.insertAndIncrement2(Lexeme.SET, Lexeme.COMPARATOR, new File(outDir, names.file("lexemes")), names.table("lexemes"), names.columns("lexemes"), header);
					Lexeme.SET.clear();
					Progress.traceDone();

					FE.BY_FETYPEID_AND_FRAMEID = makeFEByFETypeIdAndFrameIdMap();
					try
					{
						Progress.tracePending("set", "fe");
						Insert.insert2(FE.SET, FE.COMPARATOR, new File(outDir, names.file("fes")), names.table("fes"), names.columns("fes"), header);
						FE.SET.clear();
						Progress.traceDone();

						try (@ProvidesIdTo(type = FERealization.class) var ignored40 = FERealization.LIST.open(); // vu_fer
						     @ProvidesIdTo(type = FEGroupRealization.class) var ignored41 = FEGroupRealization.LIST.open())
						{
							Progress.tracePending("collector", "fer");
							Insert.insert2(FERealization.LIST, new File(outDir, names.file("ferealizations")), names.table("ferealizations"), names.columns("ferealizations"), header);
							Progress.traceDone();

							Progress.tracePending("collector", "fegr");
							Insert.insert2(FEGroupRealization.LIST, new File(outDir, names.file("fegrouprealizations")), names.table("fegrouprealizations"), names.columns("fegrouprealizations"), header);
							Progress.traceDone();

							Progress.tracePending("set", "fe_fegr");
							Insert.insert2(FE_FEGroupRealization.SET, FE_FEGroupRealization.COMPARATOR, new File(outDir, names.file("fes_fegrouprealizations")), names.table("fes_fegrouprealizations"), names.columns("fes_fegrouprealizations"), header);
							FE_FEGroupRealization.SET.clear();
							Progress.traceDone();

							try (@ProvidesIdTo(type = FEGroupPattern.class) var ignored44 = FEGroupPattern.LIST.open(); //
							     @ProvidesIdTo(type = ValenceUnit.class) var ignored43 = ValenceUnit.COLLECTOR.open())
							{
								Progress.tracePending("set", "fe_fegr");
								Insert.insert2(FEPattern.SET, FEPattern.COMPARATOR, new File(outDir, names.file("ferealizations_valenceunits")), names.table("ferealizations_valenceunits"), names.columns("ferealizations_valenceunits"), header);
								FEPattern.SET.clear();
								Progress.traceDone();

								Progress.tracePending("collector", "valenceunit");
								Insert.insert2(ValenceUnit.COLLECTOR, ValenceUnit.COLLECTOR, new File(outDir, names.file("valenceunits")), names.table("valenceunits"), names.columns("valenceunits"), header);
								Progress.traceDone();

								Progress.tracePending("collector", "grouppattern");
								Insert.insert2(FEGroupPattern.LIST, new File(outDir, names.file("grouppatterns")), names.table("grouppatterns"), names.columns("grouppatterns"), header);
								Progress.traceDone();

								Progress.tracePending("collector", "grouppattern_annoset");
								Insert.insert2(FEGroupPattern_AnnoSet.SET, (Comparator<FEGroupPattern_AnnoSet>) null, new File(outDir, names.file("grouppatterns_annosets")), names.table("grouppatterns_annosets"), names.columns("grouppatterns_annosets"), header);
								FEGroupPattern_AnnoSet.SET.clear();
								Progress.traceDone();

								Progress.tracePending("list", "grouppattern_pattern");
								Insert.insert2(FEGroupPattern_FEPattern.LIST, new File(outDir, names.file("grouppatterns_patterns")), names.table("grouppatterns_patterns"), names.columns("grouppatterns_patterns"), header);
								FEGroupPattern_FEPattern.LIST.clear();
								Progress.traceDone();

								Progress.tracePending("collector", "valenceunit_annoset");
								Insert.insert2(ValenceUnit_AnnoSet.SET, (Comparator<ValenceUnit_AnnoSet>) null, new File(outDir, names.file("valenceunits_annosets")), names.table("valenceunits_annosets"), names.columns("valenceunits_annosets"), header);
								ValenceUnit_AnnoSet.SET.clear();
								Progress.traceDone();
							}
						}

						try (@ProvidesIdTo(type = Governor.class) var ignored42 = Governor.COLLECTOR.open())
						{
							Progress.tracePending("collector", "governor");
							Insert.insert2(Governor.COLLECTOR, Governor.COLLECTOR, new File(outDir, names.file("governors")), names.table("governors"), names.columns("governors"), header);
							Progress.traceDone();

							Progress.tracePending("set", "lexunit_governor");
							Insert.insert2(LexUnit_Governor.SET, LexUnit_Governor.COMPARATOR, new File(outDir, names.file("lexunits_governors")), names.table("lexunits_governors"), names.columns("lexunits_governors"), header);
							LexUnit_Governor.SET.clear();
							Progress.traceDone();

							Progress.tracePending("set", "governor_annoset");
							Insert.insert2(Governor_AnnoSet.SET, Governor_AnnoSet.COMPARATOR, new File(outDir, names.file("governors_annosets")), names.table("governors_annosets"), names.columns("governors_annosets"), header);
							Governor_AnnoSet.SET.clear();
							Progress.traceDone();
						}

						Progress.tracePending("set", "sentence");
						Insert.insert2(Sentence.SET, Sentence.COMPARATOR, new File(outDir, names.file("sentences")), names.table("sentences"), names.columns("sentences"), header);
						Sentence.SET.clear();
						Progress.traceDone();

						try (@ProvidesIdTo(type = SubCorpus.class) var ignored50 = SubCorpus.COLLECTOR.open())
						{
							Progress.tracePending("set", "subcorpus_sentence");
							Insert.insert2(SubCorpus.COLLECTOR, SubCorpus.COLLECTOR, new File(outDir, names.file("subcorpuses")), names.table("subcorpuses"), names.columns("subcorpuses"), header);
							Progress.traceDone();

							Progress.tracePending("set", "subcorpus_sentence");
							Insert.insert2(SubCorpus_Sentence.SET, SubCorpus_Sentence.COMPARATOR, new File(outDir, names.file("subcorpuses_sentences")), names.table("subcorpuses_sentences"), names.columns("subcorpuses_sentences"), header);
							SubCorpus_Sentence.SET.clear();
							Progress.traceDone();
						}
					}
					finally
					{
						FE.BY_FETYPEID_AND_FRAMEID = null;
					}

					// R E S O L V A B L E
					insertWords();
				}
			}
		}
	}

	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		Insert.insert2(Word.COLLECTOR, Word.COLLECTOR, new File(outDir, names.file("words")), names.table("words"), names.columns("words"), header);
		Progress.traceDone();
	}

	@RequiresIdFrom(type = FeType.class)
	private Map<Pair<Integer, Integer>, FE> makeFEByFETypeIdAndFrameIdMap()
	{
		return FE.SET.stream() //
				.map(fe -> new SimpleEntry<>(new Pair<>(FeType.getIntId(fe.getName()), fe.getFrameID()), fe)) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
	}
}
