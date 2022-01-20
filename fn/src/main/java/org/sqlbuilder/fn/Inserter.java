package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.MapFactory;

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
		FnPos.MAP = MapFactory.makeSortedMap(FnPos.SET, FnPos.COMPARATOR);
		FnCoreType.MAP = MapFactory.makeSortedMap(FnCoreType.SET, FnCoreType.COMPARATOR);
		FnLabelIType.MAP = MapFactory.makeSortedMap(FnLabelIType.SET, FnLabelIType.COMPARATOR);

		Insert.insert(FnPos.MAP, new File(outDir, Names.POSES.FILE), Names.POSES.TABLE, Names.POSES.COLUMNS);
		Insert.insert(FnCoreType.MAP, new File(outDir, Names.CORETYPES.FILE), Names.CORETYPES.TABLE, Names.CORETYPES.COLUMNS);
		Insert.insert(FnLabelIType.MAP, new File(outDir, Names.LABELITYPES.FILE), Names.LABELITYPES.TABLE, Names.LABELITYPES.COLUMNS);
	}

	public void insertFrames() throws FileNotFoundException
	{
		Insert.insert(FnFrame.SET, Comparator.comparing(FnFrame::getID), new File(outDir, Names.FRAMES.FILE), Names.FRAMES.TABLE, Names.FRAMES.COLUMNS);
		Insert.insert(FnFE.SET, Comparator.comparing(FnFE::getID), new File(outDir, Names.FES.FILE), Names.FES.TABLE, Names.FES.COLUMNS);
		FnFrame.SET.clear();
		FnFE.SET.clear();
	}

	public void insertLexUnits() throws FileNotFoundException
	{
		Insert.insert(FnLexUnit.SET, null, new File(outDir, Names.LEXUNITS.FILE), Names.LEXUNITS.TABLE, Names.LEXUNITS.COLUMNS);
		FnLexUnit.SET.clear();
	}

	public void insertFullText() throws FileNotFoundException
	{
	}

	public void insertFinal() throws FileNotFoundException
	{
		FnWord.MAP = MapFactory.makeSortedMap(FnWord.SET, FnWord.COMPARATOR);
		Insert.insert(FnWord.MAP, new File(outDir, Names.WORDS.FILE), Names.WORDS.TABLE, Names.WORDS.COLUMNS);
	}

	public void insertSemTypes() throws FileNotFoundException
	{
		FnSemType.MAP = MapFactory.makeSortedMap(FnSemType.SET, FnSemType.COMPARATOR);
		Insert.insert(FnSemType.MAP, new File(outDir, Names.SEMTYPES.FILE), Names.SEMTYPES.TABLE, Names.SEMTYPES.COLUMNS);
	}
}
