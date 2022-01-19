package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SubCorpusType;

public class FnSubCorpus extends Pair<Long, SubCorpusType> implements Insertable<FnSubCorpus>
{
	public static final Set<FnSubCorpus> SET = new HashSet<>();

	public FnSubCorpus(final long luid, final SubCorpusType subcorpus)
	{
		super(luid, subcorpus);
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
		return String.format("[SUBCORPUS name=%s luid=%s]", this.second.getName(), this.first);
	}
}
