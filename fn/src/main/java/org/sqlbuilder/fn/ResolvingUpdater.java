package org.sqlbuilder.fn;

import org.sqlbuilder.common.*;
import org.sqlbuilder.fn.objects.Word;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class ResolvingUpdater extends ResolvingInserter
{
	public ResolvingUpdater(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);
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
		Update.update(Word.COLLECTOR, new File(outDir, names.updateFile("words")), names.table("words"), resolver, names.column("words.word"), names.column("words.wordid"));
		Progress.traceDone(null);
	}
}
