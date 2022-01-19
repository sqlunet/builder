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

	public void insertFrames() throws FileNotFoundException
	{
		Insert.insert(FnFrame.SET, Comparator.comparing(FnFrame::getID), new File(outDir, Names.FRAMES.FILE), Names.FRAMES.TABLE, Names.FRAMES.COLUMNS);
		Insert.insert(FnFE.SET, Comparator.comparing(FnFE::getID), new File(outDir, Names.FES.FILE), Names.FES.TABLE, Names.FES.COLUMNS);

		FnWord.MAP = MapFactory.makeSortedMap(FnWord.SET, FnWord.COMPARATOR);
		Insert.insert(FnWord.MAP, new File(outDir, Names.WORDS.FILE), Names.WORDS.TABLE, Names.WORDS.COLUMNS);
	}

	public void insertLexUnits()
	{
	}

	public void insertFullText()
	{
	}
}
