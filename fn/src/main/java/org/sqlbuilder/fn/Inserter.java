package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.MapFactory;
import org.sqlbuilder.fn.objects.*;
import org.sqlbuilder.fn.types.FeType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Properties;

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
		Values.Pos.MAP = MapFactory.makeSortedMap(Values.Pos.SET, Values.Pos.COMPARATOR);
		Values.CoreType.MAP = MapFactory.makeSortedMap(Values.CoreType.SET, Values.CoreType.COMPARATOR);
		Values.LabelIType.MAP = MapFactory.makeSortedMap(Values.LabelIType.SET, Values.LabelIType.COMPARATOR);

		Insert.insert(Values.Pos.MAP, new File(outDir, Names.POSES.FILE), Names.POSES.TABLE, Names.POSES.COLUMNS);
		Insert.insert(Values.CoreType.MAP, new File(outDir, Names.CORETYPES.FILE), Names.CORETYPES.TABLE, Names.CORETYPES.COLUMNS);
		Insert.insert(Values.LabelIType.MAP, new File(outDir, Names.LABELITYPES.FILE), Names.LABELITYPES.TABLE, Names.LABELITYPES.COLUMNS);
	}

	public void insertFrames() throws FileNotFoundException
	{
		Insert.insert(Frame.SET, Comparator.comparing(Frame::getID), new File(outDir, Names.FRAMES.FILE), Names.FRAMES.TABLE, Names.FRAMES.COLUMNS);
		Frame.SET.clear();
	}

	public void insertLexUnits() throws FileNotFoundException
	{
		Insert.insert(LexUnit.SET, null, new File(outDir, Names.LEXUNITS.FILE), Names.LEXUNITS.TABLE, Names.LEXUNITS.COLUMNS);
		LexUnit.SET.clear();
	}

	public void insertFullText() throws FileNotFoundException
	{
	}

	public void insertFinal() throws FileNotFoundException
	{
		Word.MAP = MapFactory.makeSortedMap(Word.SET, Word.COMPARATOR);
		Insert.insert(Word.MAP, new File(outDir, Names.WORDS.FILE), Names.WORDS.TABLE, Names.WORDS.COLUMNS);

		FeType.MAP = MapFactory.makeSortedMap(FeType.SET,Comparator.naturalOrder());
		Insert.insertStringMap(FeType.MAP, new File(outDir, Names.FETYPES.FILE), Names.FETYPES.TABLE, Names.FETYPES.COLUMNS);

		Insert.insert(FE.SET, Comparator.comparing(FE::getID), new File(outDir, Names.FES.FILE), Names.FES.TABLE, Names.FES.COLUMNS);
		FE.SET.clear();
	}

	public void insertSemTypes() throws FileNotFoundException
	{
		Insert.insert(SemType.SET, SemType.COMPARATOR, new File(outDir, Names.SEMTYPES.FILE), Names.SEMTYPES.TABLE, Names.SEMTYPES.COLUMNS);
	}
}
