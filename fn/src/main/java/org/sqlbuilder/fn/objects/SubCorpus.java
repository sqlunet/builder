package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SubCorpusType;

public class SubCorpus implements Insertable<SubCorpus>
{
	public static final Set<SubCorpus> SET = new HashSet<>();

	private final SubCorpusType subcorpus;

	private final int luid;

	public SubCorpus(final SubCorpusType subcorpus, final int luid)
	{
		this.subcorpus = subcorpus;
		this.luid = luid;
	}

	@Override
	public String dataRow()
	{
		return String.format("NULL,'%s',%d",
				// getId(),
				subcorpus.getName(), //
				luid);
	}

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS name=%s]", subcorpus.getName());
	}
}
