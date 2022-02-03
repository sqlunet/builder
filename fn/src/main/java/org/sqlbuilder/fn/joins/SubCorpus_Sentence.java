package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.objects.Sentence;
import org.sqlbuilder.fn.objects.SubCorpus;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SubCorpus_Sentence extends Pair<SubCorpus, Integer> implements Insertable
{
	public static final Comparator<SubCorpus_Sentence> COMPARATOR = Comparator.comparing(SubCorpus_Sentence::getFirst, SubCorpus.COMPARATOR).thenComparing(SubCorpus_Sentence::getSecond);

	public static final Set<SubCorpus_Sentence> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static SubCorpus_Sentence make(final SubCorpus subcorpus, final Sentence sentence)
	{
		var ss = new SubCorpus_Sentence(subcorpus, sentence.getID());
		SET.add(ss);
		return ss;
	}

	private SubCorpus_Sentence(final SubCorpus subcorpus, final Integer sentenceid)
	{
		super(subcorpus, sentenceid);
	}

	// I N S E R T

	@RequiresIdFrom(type = SubCorpus.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%d", first.getSqlId(), second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS-SENT subcorpusid=%s sentenceid=%s]", first, second);
	}
}
