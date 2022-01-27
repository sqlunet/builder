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
		this.outDir = new File(conf.getProperty("outdir", "fn"));
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
		{
			Progress.traceHeader("insert", "semtype");
			Insert.insert(SemType.SET, SemType.COMPARATOR, new File(outDir, Names.SEMTYPES.FILE), Names.SEMTYPES.TABLE, Names.SEMTYPES.COLUMNS);
			SemType.SET.clear();
			Progress.traceTailer("insert", "done");

			Progress.traceHeader("insert", "semtype_super");
			Insert.insert(SemType_SemTypeSuper.SET, SemType_SemTypeSuper.COMPARATOR, new File(outDir, Names.SEMTYPES_SUPERS.FILE), Names.SEMTYPES_SUPERS.TABLE, Names.SEMTYPES_SUPERS.COLUMNS);
			SemType_SemTypeSuper.SET.clear();
			Progress.traceTailer("insert", "done");
		}
	}

	public void insertFrames() throws FileNotFoundException
	{
		Progress.traceHeader("insert", "frame");
		Insert.insert(Frame.SET, Comparator.comparing(Frame::getID), new File(outDir, Names.FRAMES.FILE), Names.FRAMES.TABLE, Names.FRAMES.COLUMNS);
		Frame.SET.clear();
		Progress.traceTailer("insert", "done");

		try (var ignored = FrameRelation.COLLECTOR.open())
		{
			Progress.traceHeader("map+insert", "frame_relations");
			Insert.insertStringMap(FrameRelation.COLLECTOR, new File(outDir, Names.FRAMERELATIONS.FILE), Names.FRAMERELATIONS.TABLE, Names.FRAMERELATIONS.COLUMNS);
			Progress.traceTailer("map+insert", "done");

			Progress.traceHeader("insert", "frame_related");
			Insert.insert(Frame_FrameRelated.SET, Frame_FrameRelated.COMPARATOR, new File(outDir, Names.FRAMES_RELATED.FILE), Names.FRAMES_RELATED.TABLE, Names.FRAMES_RELATED.COLUMNS);
			Frame_FrameRelated.SET.clear();
			Progress.traceTailer("insert", "done");
		}

		Progress.traceHeader("insert", "fe_required");
		Insert.insert(FE_FERequired.SET, FE_FERequired.COMPARATOR, new File(outDir, Names.FES_REQUIRED.FILE), Names.FES_REQUIRED.TABLE, Names.FES_REQUIRED.COLUMNS);
		FE_FERequired.SET.clear();
		Progress.traceTailer("insert", "done");

		Progress.traceHeader("insert", "fe_excluded");
		Insert.insert(FE_FEExcluded.SET, FE_FEExcluded.COMPARATOR, new File(outDir, Names.FES_EXCLUDED.FILE), Names.FES_EXCLUDED.TABLE, Names.FES_EXCLUDED.COLUMNS);
		FE_FEExcluded.SET.clear();
		Progress.traceTailer("insert", "done");

		Progress.traceHeader("insert", "frame_semtype");
		Insert.insert(Frame_SemType.SET, Frame_SemType.COMPARATOR, new File(outDir, Names.FRAMES_SEMTYPES.FILE), Names.FRAMES_SEMTYPES.TABLE, Names.FRAMES_SEMTYPES.COLUMNS);
		Frame_SemType.SET.clear();
		Progress.traceTailer("insert", "done");
	}

	public void insertLexUnits() throws FileNotFoundException
	{
	}

	public void insertFullText() throws FileNotFoundException
	{
	}

	public void insertFinal() throws FileNotFoundException
	{

		try ( //
		      var ignored01 = Values.Pos.COLLECTOR.open(); //
		      var ignored02 = Values.CoreType.COLLECTOR.open(); //
		      var ignored03 = Values.LabelIType.COLLECTOR.open(); //
		      var ignored11 = GfType.COLLECTOR.open(); //
		      var ignored12 = PtType.COLLECTOR.open(); //
		      var ignored13 = LayerType.COLLECTOR.open(); //
		      var ignored14 = LabelType.COLLECTOR.open() //
		)
		{
			Progress.traceHeader("insert", "pos,coretype,labelitype");
			Insert.insert(Values.Pos.COLLECTOR, new File(outDir, Names.POSES.FILE), Names.POSES.TABLE, Names.POSES.COLUMNS);
			Insert.insert(Values.CoreType.COLLECTOR, new File(outDir, Names.CORETYPES.FILE), Names.CORETYPES.TABLE, Names.CORETYPES.COLUMNS);
			Insert.insert(Values.LabelIType.COLLECTOR, new File(outDir, Names.LABELITYPES.FILE), Names.LABELITYPES.TABLE, Names.LABELITYPES.COLUMNS);
			Progress.traceTailer("insert", "done");

			Progress.traceHeader("collector", "gf");
			Insert.insertStringMap(GfType.COLLECTOR, new File(outDir, Names.GFTYPES.FILE), Names.GFTYPES.TABLE, Names.GFTYPES.COLUMNS);
			Progress.traceTailer("collector", "done");

			Progress.traceHeader("collector", "pt");
			Insert.insertStringMap(PtType.COLLECTOR, new File(outDir, Names.PTTYPES.FILE), Names.PTTYPES.TABLE, Names.PTTYPES.COLUMNS);
			Progress.traceTailer("collector", "done");

			Progress.traceHeader("collector", "layertypes");
			Insert.insertStringMap(LayerType.COLLECTOR, new File(outDir, Names.LAYERTYPES.FILE), Names.LAYERTYPES.TABLE, Names.LAYERTYPES.COLUMNS);
			Progress.traceTailer("collector", "done");

			Progress.traceHeader("collector", "labeltypes");
			Insert.insertStringMap(LabelType.COLLECTOR, new File(outDir, Names.LABELTYPES.FILE), Names.LABELTYPES.TABLE, Names.LABELTYPES.COLUMNS);
			Progress.traceTailer("collector", "done");

			Progress.traceHeader("insert", "annoset");
			Insert.insert(AnnotationSet.SET, AnnotationSet.COMPARATOR, new File(outDir, Names.ANNOSETS.FILE), Names.ANNOSETS.TABLE, Names.ANNOSETS.COLUMNS);
			AnnotationSet.SET.clear();
			Progress.traceTailer("insert", "done");

			Progress.traceHeader("insert", "corpus");
			Insert.insert(Corpus.SET, Corpus.COMPARATOR, new File(outDir, Names.CORPUSES.FILE), Names.CORPUSES.TABLE, Names.CORPUSES.COLUMNS);
			Corpus.SET.clear();
			Progress.traceTailer("insert", "done");

			Progress.traceHeader("insert", "doc");
			Insert.insert(Doc.SET, Doc.COMPARATOR, new File(outDir, Names.DOCUMENTS.FILE), Names.DOCUMENTS.TABLE, Names.DOCUMENTS.COLUMNS);
			Doc.SET.clear();
			Progress.traceTailer("insert", "done");

			try (var ignored = Layer.COLLECTOR.open())
			{
				Progress.traceHeader("collector", "layer");
				Insert.insert(Layer.COLLECTOR, new File(outDir, Names.LAYERS.FILE), Names.LAYERS.TABLE, Names.LAYERS.COLUMNS);
				Progress.traceTailer("collector", "done");

				Progress.traceHeader("insert", "label");
				Insert.insert(Label.SET, Label.COMPARATOR, new File(outDir, Names.LABELS.FILE), Names.LABELS.TABLE, Names.LABELS.COLUMNS);
				Label.SET.clear();
				Progress.traceTailer("insert", "done");
			}


			try (var ignored = FeType.COLLECTOR.open())
			{
				Progress.traceHeader("collector", "fetype");
				Insert.insertStringMap(FeType.COLLECTOR, new File(outDir, Names.FETYPES.FILE), Names.FETYPES.TABLE, Names.FETYPES.COLUMNS);
				Progress.traceTailer("collector", "done");

				Progress.traceHeader("insert", "lexunit");
				Insert.insert(LexUnit.SET, LexUnit.COMPARATOR, new File(outDir, Names.LEXUNITS.FILE), Names.LEXUNITS.TABLE, Names.LEXUNITS.COLUMNS);
				LexUnit.SET.clear();
				Progress.traceTailer("insert", "done");

				Progress.traceHeader("insert", "lexunit_semtype");
				Insert.insert(LexUnit_SemType.SET, LexUnit_SemType.COMPARATOR, new File(outDir, Names.LEXUNITS_SEMTYPES.FILE), Names.LEXUNITS_SEMTYPES.TABLE, Names.LEXUNITS_SEMTYPES.COLUMNS);
				LexUnit_SemType.SET.clear();
				Progress.traceTailer("insert", "done");

				Progress.traceHeader("insert", "lexunit_semtype");
				Insert.insert(FE_SemType.SET, FE_SemType.COMPARATOR, new File(outDir, Names.FES_SEMTYPES.FILE), Names.FES_SEMTYPES.TABLE, Names.FES_SEMTYPES.COLUMNS);
				FE_SemType.SET.clear();
				Progress.traceTailer("insert", "done");

				try (var ignored30 = Word.COLLECTOR.open())
				{
					Progress.traceHeader("collector", "word");
					Insert.insert(Word.COLLECTOR, new File(outDir, Names.WORDS.FILE), Names.WORDS.TABLE, Names.WORDS.COLUMNS);
					Progress.traceTailer("collector", "done");

					Progress.traceHeader("insert", "lexeme");
					Insert.insertAndIncrement(Lexeme.SET, Lexeme.COMPARATOR, new File(outDir, Names.LEXEMES.FILE), Names.LEXEMES.TABLE, Names.LEXEMES.COLUMNS);
					Lexeme.SET.clear();
					Progress.traceTailer("insert", "done");
				}

				FE.BY_FETYPEID_AND_FRAMEID = makeFEByFETypeIdAndFrameIdMap();
				try
				{
					Progress.traceHeader("insert", "fe");
					Insert.insert(FE.SET, FE.COMPARATOR, new File(outDir, Names.FES.FILE), Names.FES.TABLE, Names.FES.COLUMNS);
					FE.SET.clear();
					Progress.traceTailer("insert", "done");

					try (var ignored40 = FERealization.COLLECTOR.open()) // vu_fer
					{
						Progress.traceHeader("collector", "fer");
						Insert.insert(FERealization.COLLECTOR, new File(outDir, Names.FEREALIZATIONS.FILE), Names.FEREALIZATIONS.TABLE, Names.FEREALIZATIONS.COLUMNS);
						Progress.traceTailer("map+insert", "done");
					}

					try (var ignored41 = FEGroupRealization.COLLECTOR.open())
					{
						Progress.traceHeader("collector", "fegr");
						Insert.insert(FEGroupRealization.COLLECTOR, new File(outDir, Names.FEGROUPREALIZATIONS.FILE), Names.FEGROUPREALIZATIONS.TABLE, Names.FEGROUPREALIZATIONS.COLUMNS);
						Progress.traceTailer("map+insert", "done");

						Progress.traceHeader("insert", "fe_fegr");
						Insert.insert(FE_FEGroupRealization.SET, FE_FEGroupRealization.COMPARATOR, new File(outDir, Names.FES_FEGROUPREALIZATIONS.FILE), Names.FES_FEGROUPREALIZATIONS.TABLE, Names.FES_FEGROUPREALIZATIONS.COLUMNS);
						FE_FEGroupRealization.SET.clear();
						Progress.traceTailer("insert", "done");
					}

					try (var ignored42 = Governor.COLLECTOR.open())
					{
						Progress.traceHeader("collector", "governor");
						Insert.insert(Governor.COLLECTOR, new File(outDir, Names.GOVERNORS.FILE), Names.GOVERNORS.TABLE, Names.GOVERNORS.COLUMNS);
						Progress.traceTailer("collector", "done");

						Progress.traceHeader("insert", "lexunits_governors");
						Insert.insert(LexUnit_Governor.SET, null, new File(outDir, Names.LEXUNITS_GOVERNORS.FILE), Names.LEXUNITS_GOVERNORS.TABLE, Names.LEXUNITS_GOVERNORS.COLUMNS);
						LexUnit_Governor.SET.clear();
						Progress.traceTailer("insert", "done");

						Progress.traceHeader("insert", "governors_annosets");
						Insert.insert(Governor_AnnoSet.SET, null, new File(outDir, Names.GOVERNORS_ANNOSETS.FILE), Names.GOVERNORS_ANNOSETS.TABLE, Names.GOVERNORS_ANNOSETS.COLUMNS);
						Governor_AnnoSet.SET.clear();
						Progress.traceTailer("insert", "done");
					}
				}
				finally
				{
					FE.BY_FETYPEID_AND_FRAMEID = null;
				}
			}
		}
	}

	private Map<Pair<Integer, Integer>, FE> makeFEByFETypeIdAndFrameIdMap()
	{
		return FE.SET.stream() //
				.map(fe -> new SimpleEntry<>(new Pair<>(FeType.COLLECTOR.get(fe.getName()), fe.getFrameID()), fe)) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
	}
}
