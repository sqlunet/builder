package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

public class FnSubCorpus_Sentence extends Pair<FnSubCorpus, FnSentence>
{
	public static final Set<FnSubCorpus_Sentence> SET = new HashSet<>();

	public FnSubCorpus_Sentence(final FnSubCorpus subcorpus, final FnSentence sentence)
	{
		super(subcorpus, sentence);
	}

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS-SENT subcorpusid=%s sentenceid=%s]", this.first, this.second);
	}
}
