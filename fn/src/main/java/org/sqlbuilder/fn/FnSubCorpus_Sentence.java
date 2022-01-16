package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnSubCorpus_Sentence extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_subcorpuses_sentences.insert");

	private static final String TABLE = Resources.resources.getString("Fn_subcorpuses_sentences.table");

	public FnSubCorpus_Sentence(final long subcorpusid, final long sentenceid)
	{
		super(subcorpusid, sentenceid);
	}

	@Override
	protected String getSql()
	{
		return FnSubCorpus_Sentence.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnSubCorpus_Sentence.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS-SENT subcorpusid=%s sentenceid=%s]", this.id1, this.id2);
	}
}
