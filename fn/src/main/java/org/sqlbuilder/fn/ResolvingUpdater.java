package org.sqlbuilder.fn;

import org.sqlbuilder.common.Progress;
import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.common.Update;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.objects.Word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ResolvingUpdater extends ResolvingInserter
{
	public ResolvingUpdater(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("fn_outdir_updated", "sql/data_updated"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	@Override
	public void insert() throws FileNotFoundException
	{
		try (@ProvidesIdTo(type = Word.class) var ignored30 = Word.COLLECTOR.open())
		{
			insertWords();
		}
	}

	@Override
	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		final String wordidCol = names.column("words.wordid");
		final String wordCol = names.column("words.word");
		Update.update(Word.COLLECTOR, new File(outDir, names.updateFile("words")), header, names.table("words"), //
				resolver, //
				resolved -> wordidCol + '=' + Utils.nullableInt(resolved), //
				resolving -> String.format("%s='%s'", wordCol, Utils.escape(resolving)));
		Progress.traceDone();
	}
}
