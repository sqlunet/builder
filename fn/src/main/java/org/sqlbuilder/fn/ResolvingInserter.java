package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.objects.Word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ResolvingInserter extends Inserter
{
	protected final String serFile;

	protected final FnWordResolver resolver;

	public ResolvingInserter(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// header
		this.header += "\n-- " + conf.getProperty("wn_resolve_against");

		// output
		this.outDir = new File(conf.getProperty("fn_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}

		// resolve
		this.serFile = conf.getProperty("word_nids");
		this.resolver = new FnWordResolver(this.serFile);
	}

	@Override
	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		Insert.resolveAndInsert(Word.COLLECTOR, new File(outDir, names.file("words")), names.table("words"), names.columns("words"), header, true, //
				resolver, //
				w -> Utils.nullable(w, Objects::toString), //
				names.column("words.wordid"));
		Progress.traceDone();
	}
}
