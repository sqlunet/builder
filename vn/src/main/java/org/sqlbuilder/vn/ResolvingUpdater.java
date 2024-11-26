package org.sqlbuilder.vn;

import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.common.Progress;
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
		Update.update2(Word.COLLECTOR, new File(outDir, names.updateFile("words")), header, names.table("words"), //
				wordResolver, //
				resolved -> String.format("%s=%s", wordidCol, Utils.nullableInt(resolved)), //
				resolving -> String.format("%s='%s'", wordCol, Utils.escape(resolving)));
		Progress.traceDone();
	}

	@Override
	protected void insertMemberSenses() throws FileNotFoundException
	{
		Progress.tracePending("set", "members senses");
		final String wordidCol = names.column("members_senses.wordid");
		final String synsetidCol = names.column("members_senses.synsetid");
		final String sensekeyCol = names.column("members_senses.sensekey");
		Update.update(Sense.SET, new File(outDir, names.updateFile("members_senses")), header, names.table("members_senses"), //
				sensekeyResolver, //
				resolved -> resolved == null ? String.format("%s=NULL,%s=NULL", wordidCol, synsetidCol) : String.format("%s=%s,%s=%s", wordidCol, Utils.nullableInt(resolved.getKey()), synsetidCol, Utils.nullableInt(resolved.getValue())), //
				resolving -> String.format("%s='%s'", sensekeyCol, Utils.escape(resolving)));
		Progress.traceDone();
	}
}
