package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.MapFactory;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.fn.joins.*;
import org.sqlbuilder.fn.objects.*;
import org.sqlbuilder.fn.types.FeType;
import org.sqlbuilder.fn.types.FrameRelation;

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
		Progress.traceHeader("maps", "pos,coretype,labelitype");
		Values.Pos.MAP = MapFactory.makeSortedMap(Values.Pos.SET, Values.Pos.COMPARATOR);
		Values.Pos.SET.clear();
		Values.CoreType.MAP = MapFactory.makeSortedMap(Values.CoreType.SET, Values.CoreType.COMPARATOR);
		Values.CoreType.SET.clear();
		Values.LabelIType.MAP = MapFactory.makeSortedMap(Values.LabelIType.SET, Values.LabelIType.COMPARATOR);
		Values.LabelIType.SET.clear();
		Progress.traceTailer("maps", "done");

		Progress.traceHeader("insert", "pos,coretype,labelitype");
		Insert.insert(Values.Pos.MAP, new File(outDir, Names.POSES.FILE), Names.POSES.TABLE, Names.POSES.COLUMNS);
		Insert.insert(Values.CoreType.MAP, new File(outDir, Names.CORETYPES.FILE), Names.CORETYPES.TABLE, Names.CORETYPES.COLUMNS);
		Insert.insert(Values.LabelIType.MAP, new File(outDir, Names.LABELITYPES.FILE), Names.LABELITYPES.TABLE, Names.LABELITYPES.COLUMNS);
		Progress.traceTailer("insert", "done");
	}

	public void insertSemTypes() throws FileNotFoundException
	{
		{
			Progress.traceHeader("insert", "semtype");
			Insert.insert(SemType.SET, SemType.COMPARATOR, new File(outDir, Names.SEMTYPES.FILE), Names.SEMTYPES.TABLE, Names.SEMTYPES.COLUMNS);
			SemType.SET.clear();
			Progress.traceTailer("insert", "done");
		}
	}

	public void insertFrames() throws FileNotFoundException
	{
		{
			Progress.traceHeader("insert", "frame");
			Insert.insert(Frame.SET, Comparator.comparing(Frame::getID), new File(outDir, Names.FRAMES.FILE), Names.FRAMES.TABLE, Names.FRAMES.COLUMNS);
			Frame.SET.clear();
			Progress.traceTailer("insert", "done");
		}
		{
			Progress.traceHeader("map+insert", "frame_relations");
			FrameRelation.MAP = MapFactory.makeSortedMap(FrameRelation.SET, Comparator.naturalOrder());
			FrameRelation.SET.clear();
			Insert.insertStringMap(FrameRelation.MAP, new File(outDir, Names.FRAMERELATIONS.FILE), Names.FRAMERELATIONS.TABLE, Names.FRAMERELATIONS.COLUMNS);
			Progress.traceTailer("map+insert", "done");
			{
				Progress.traceHeader("insert", "frame_related");
				Insert.insert(Frame_FrameRelated.SET, Frame_FrameRelated.COMPARATOR, new File(outDir, Names.FRAMES_RELATED.FILE), Names.FRAMES_RELATED.TABLE, Names.FRAMES_RELATED.COLUMNS);
				Frame_FrameRelated.SET.clear();
				Progress.traceTailer("insert", "done");
			}
		}
		{
			Progress.traceHeader("insert", "fe_required");
			Insert.insert(FE_FERequired.SET, FE_FERequired.COMPARATOR, new File(outDir, Names.FES_REQUIRED.FILE), Names.FES_REQUIRED.TABLE, Names.FES_REQUIRED.COLUMNS);
			FE_FERequired.SET.clear();
			Progress.traceTailer("insert", "done");
		}
		{
			Progress.traceHeader("insert", "fe_excluded");
			Insert.insert(FE_FEExcluded.SET, FE_FEExcluded.COMPARATOR, new File(outDir, Names.FES_EXCLUDED.FILE), Names.FES_EXCLUDED.TABLE, Names.FES_EXCLUDED.COLUMNS);
			FE_FEExcluded.SET.clear();
			Progress.traceTailer("insert", "done");
		}
	}

	public void insertLexUnits() throws FileNotFoundException
	{
	}

	public void insertFullText() throws FileNotFoundException
	{
	}

	public void insertFinal() throws FileNotFoundException
	{
		Progress.traceHeader("insert", "annoset");
		Insert.insert(AnnotationSet.SET, AnnotationSet.COMPARATOR, new File(outDir, Names.ANNOSETS.FILE), Names.ANNOSETS.TABLE, Names.ANNOSETS.COLUMNS);
		AnnotationSet.SET.clear();
		Progress.traceTailer("insert", "done");

		{
			Progress.traceHeader("map+insert", "layer");
			Layer.MAP = MapFactory.makeSortedMap(Layer.SET, Layer.COMPARATOR);
			Layer.SET.clear();
			Insert.insert(Layer.MAP, new File(outDir, Names.LAYERS.FILE), Names.LAYERS.TABLE, Names.LAYERS.COLUMNS);
			Progress.traceTailer("map+insert", "done");

			Progress.traceHeader("insert", "label");
			Insert.insert(Label.SET, Label.COMPARATOR, new File(outDir, Names.LABELS.FILE), Names.LABELS.TABLE, Names.LABELS.COLUMNS);
			Label.SET.clear();
			Progress.traceTailer("insert", "done");

			Layer.MAP = null;
		}

		{
			Progress.traceHeader("insert", "corpus");
			Insert.insert(Corpus.SET, Corpus.COMPARATOR, new File(outDir, Names.CORPUSES.FILE), Names.CORPUSES.TABLE, Names.CORPUSES.COLUMNS);
			Corpus.SET.clear();
			Progress.traceTailer("insert", "done");
		}

		{
			Progress.traceHeader("insert", "doc");
			Insert.insert(Doc.SET, Doc.COMPARATOR, new File(outDir, Names.DOCUMENTS.FILE), Names.DOCUMENTS.TABLE, Names.DOCUMENTS.COLUMNS);
			Doc.SET.clear();
			Progress.traceTailer("insert", "done");
		}

		Progress.traceHeader("map+insert", "fetype");
		FeType.MAP = MapFactory.makeSortedMap(FeType.SET, Comparator.naturalOrder());
		FeType.SET.clear();
		Insert.insertStringMap(FeType.MAP, new File(outDir, Names.FETYPES.FILE), Names.FETYPES.TABLE, Names.FETYPES.COLUMNS);
		Progress.traceTailer("map+insert", "done");

		Progress.traceHeader("insert", "lexunit");
		Insert.insert(LexUnit.SET, LexUnit.COMPARATOR, new File(outDir, Names.LEXUNITS.FILE), Names.LEXUNITS.TABLE, Names.LEXUNITS.COLUMNS);
		LexUnit.SET.clear();
		Progress.traceTailer("insert", "done");

		Progress.traceHeader("map+insert", "word");
		Word.MAP = MapFactory.makeSortedMap(Word.SET, Word.COMPARATOR);
		Word.SET.clear();
		Insert.insert(Word.MAP, new File(outDir, Names.WORDS.FILE), Names.WORDS.TABLE, Names.WORDS.COLUMNS);
		Progress.traceTailer("map+insert", "done");

		Progress.traceHeader("insert", "lexeme");
		Insert.insertAndIncrement(Lexeme.SET, Lexeme.COMPARATOR, new File(outDir, Names.LEXEMES.FILE), Names.LEXEMES.TABLE, Names.LEXEMES.COLUMNS);
		Lexeme.SET.clear();
		Progress.traceTailer("insert", "done");

		{
			Progress.traceHeader("map", "fe by fetype and frame");
			FE.BY_FETYPEID_AND_FRAMEID = makeFEByFETypeIdAndFrameIdMap();
			Progress.traceTailer("map", "done");

			Progress.traceHeader("insert", "fe");
			Insert.insert(FE.SET, FE.COMPARATOR, new File(outDir, Names.FES.FILE), Names.FES.TABLE, Names.FES.COLUMNS);
			FE.SET.clear();
			Progress.traceTailer("insert", "done");

			{
				Progress.traceHeader("map+insert", "fer");
				FERealization.MAP = MapFactory.makeSortedMap(FERealization.SET, FERealization.COMPARATOR); // vu_fer
				FERealization.SET.clear();
				Insert.insert(FERealization.MAP, new File(outDir, Names.FEREALIZATIONS.FILE), Names.FEREALIZATIONS.TABLE, Names.FEREALIZATIONS.COLUMNS);
				Progress.traceTailer("map+insert", "done");

				FERealization.MAP = null;
			}

			{
				Progress.traceHeader("map+insert", "fegr");
				FEGroupRealization.MAP = MapFactory.makeSortedMap(FEGroupRealization.SET, FEGroupRealization.COMPARATOR);
				FEGroupRealization.SET.clear();
				Insert.insert(FEGroupRealization.MAP, new File(outDir, Names.FEGROUPREALIZATIONS.FILE), Names.FEGROUPREALIZATIONS.TABLE, Names.FEGROUPREALIZATIONS.COLUMNS);
				Progress.traceTailer("map+insert", "done");

				Progress.traceHeader("insert", "fe_fegr");
				Insert.insert(FE_FEGroupRealization.SET, FE_FEGroupRealization.COMPARATOR, new File(outDir, Names.FES_FEGROUPREALIZATIONS.FILE), Names.FES_FEGROUPREALIZATIONS.TABLE, Names.FES_FEGROUPREALIZATIONS.COLUMNS);
				FE_FEGroupRealization.SET.clear();
				Progress.traceTailer("insert", "done");

				FEGroupRealization.MAP = null;
			}

			{
				Progress.traceHeader("map+insert", "governor");
				Governor.MAP = MapFactory.makeSortedMap(Governor.SET, Governor.COMPARATOR);
				Governor.SET.clear();
				Insert.insert(Governor.MAP, new File(outDir, Names.GOVERNORS.FILE), Names.GOVERNORS.TABLE, Names.GOVERNORS.COLUMNS);
				Progress.traceTailer("map+insert", "done");

				Progress.traceHeader("insert", "lexunits_governors");
				Insert.insert(LexUnit_Governor.SET, null, new File(outDir, Names.LEXUNITS_GOVERNORS.FILE), Names.LEXUNITS_GOVERNORS.TABLE, Names.LEXUNITS_GOVERNORS.COLUMNS);
				LexUnit_Governor.SET.clear();
				Progress.traceTailer("insert", "done");

				Progress.traceHeader("insert", "governors_annosets");
				Insert.insert(Governor_AnnoSet.SET, null, new File(outDir, Names.GOVERNORS_ANNOSETS.FILE), Names.GOVERNORS_ANNOSETS.TABLE, Names.GOVERNORS_ANNOSETS.COLUMNS);
				Governor_AnnoSet.SET.clear();
				Progress.traceTailer("insert", "done");

				Governor.MAP = null;
			}


		}
	}

	private Map<Pair<Integer, Integer>, FE> makeFEByFETypeIdAndFrameIdMap()
	{
		return FE.SET.stream() //
				.map(fe -> new SimpleEntry<>(new Pair<>(FeType.MAP.get(fe.getName()), fe.getFrameID()), fe)) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
	}
}
