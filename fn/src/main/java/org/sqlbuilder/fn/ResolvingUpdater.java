package org.sqlbuilder.fn;

import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.ProvidesIdTo;
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
		Update.update(Word.COLLECTOR, new File(outDir, names.updateFile("words")), names.table("words"), //
				resolver, //
				resolved -> wordidCol + '=' + Utils.nullable(resolved, Object::toString), //
				names.column("words.word"));
		Progress.traceDone(null);
	}
}
