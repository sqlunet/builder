package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.objects.Sentence;
import org.sqlbuilder.fn.objects.SubCorpus;

import java.util.HashSet;
import java.util.Set;

public class SubCorpus_Sentence extends Pair<SubCorpus, Sentence>
{
	public static final Set<SubCorpus_Sentence> SET = new HashSet<>();

	public SubCorpus_Sentence(final SubCorpus subcorpus, final Sentence sentence)
	{
		super(subcorpus, sentence);
	}

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS-SENT subcorpusid=%s sentenceid=%s]", this.first, this.second);
	}
}
