package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.vn.joins.Member_Sense;
import org.sqlbuilder.vn.objects.Word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ResolvingInserter extends Inserter
{
	protected final String wordSerFile;

	protected final String sensekeySerFile;

	protected final VnWordResolver wordResolver;

	protected final VnSensekeyResolver sensekeyResolver;

	public ResolvingInserter(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("vn_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}

		// resolve
		this.wordSerFile = conf.getProperty("word_nids");
		this.sensekeySerFile = conf.getProperty("sense_nids");
		this.wordResolver = new VnWordResolver(this.wordSerFile);
		this.sensekeyResolver = new VnSensekeyResolver(this.sensekeySerFile);
	}

	@Override
	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		Insert.resolveAndInsert(Word.COLLECTOR, new File(outDir, names.file("words")), names.table("words"), names.columns("words"), true, //
				wordResolver, //
				Objects::toString, //
				names.column("words.wordid"));
		Progress.traceDone();
	}

	@Override
	protected void insertMemberSenses() throws FileNotFoundException
	{
		Progress.tracePending("set", "member sense");
		Insert.resolveAndInsert(Member_Sense.SET, Member_Sense.COMPARATOR, new File(outDir, names.file("members_senses")), names.table("members_senses"), names.columns("members_senses"), //
				sensekeyResolver, //
				Member_Sense.RESOLVE_RESULT_STRINGIFIER, //
				names.column("members_senses.wordid"),  //
				names.column("members_senses.synsetid"));
		Progress.traceDone();
	}
}
