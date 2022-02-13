package org.sqlbuilder.fn;

import org.sqlbuilder.common.*;
import org.sqlbuilder.fn.objects.Word;

import java.io.*;
import java.util.Properties;

public class ResolvingInserter extends Inserter
{
	protected final String serFile;

	protected final FnResolver resolver;

	public ResolvingInserter(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("fn_outdir_resolved", "sql/data"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}

		// resolve
		this.resolve = true;
		this.serFile = conf.getProperty("word_nids");
		this.resolver = new FnResolver(this.serFile);
	}

	@Override
	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		Insert.resolveAndInsert(Word.COLLECTOR, new File(outDir, names.file("words")), names.table("words"), names.columns("words"), resolver, names.column("words.wordid"), true);
		Progress.traceDone(null);
	}
}
