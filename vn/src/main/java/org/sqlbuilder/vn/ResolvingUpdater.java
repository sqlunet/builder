package org.sqlbuilder.vn;

import org.sqlbuilder.common.Progress;
import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.common.Update;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.vn.objects.Sense;
import org.sqlbuilder.vn.objects.Word;

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
		this.outDir = new File(conf.getProperty("vn_outdir_updated", "sql/data_updated"));
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
			insertMemberSenses();
		}
	}

	@Override
	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		final String wordidCol = names.column("words.wordid");
		final String wordCol = names.column("words.word");
		Update.update(Word.COLLECTOR.keySet(), new File(outDir, names.updateFile("words")), names.table("words"), //
				wordResolver, //
				resolved -> wordidCol + '=' + Utils.nullable(resolved, Object::toString), //
				resolving -> String.format("%s='%s'", wordCol, resolving));
		Progress.traceDone();
	}

	@Override
	protected void insertMemberSenses() throws FileNotFoundException
	{
		Progress.tracePending("set", "members senses");
		final String wordidCol = names.column("members_senses.wordid");
		final String synsetidCol = names.column("members_senses.synsetid");
		final String sensekeyCol = names.column("members_senses.sensekey");
		Update.update(Sense.SET, new File(outDir, names.updateFile("members_senses")), names.table("members_senses"), //
				sensekeyResolver, //
				resolved -> resolved == null ? "NULL,NULL" : (wordidCol + '=' + resolved.getKey() + ',' + synsetidCol + '=' + resolved.getValue()), //
				resolving -> String.format("%s='%s'", sensekeyCol, resolving));
		Progress.traceDone();
	}
}
