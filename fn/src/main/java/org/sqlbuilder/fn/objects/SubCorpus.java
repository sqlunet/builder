package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SubCorpusType;

public class SubCorpus implements Insertable<SubCorpus>
{
	public static final Set<SubCorpus> SET = new HashSet<>();

	private final SubCorpusType subcorpus;

	private final long luid;

	public SubCorpus(final SubCorpusType subcorpus, final long luid)
	{
		this.subcorpus = subcorpus;
		this.luid = luid;
	}

	@Override
	public String dataRow()
	{
		// Long(1, getId());
		// Long(2, this.luid);
		// String(3, this.subcorpus.getName());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS name=%s]", subcorpus.getName());
	}
}
