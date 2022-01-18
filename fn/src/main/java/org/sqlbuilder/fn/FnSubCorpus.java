package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SubCorpusType;

public class FnSubCorpus implements Insertable<FnSubCorpus>
{
	public static final Set<FnSubCorpus> SET = new HashSet<>();

	public final SubCorpusType subcorpus;

	public final FnLexUnit lu;

	public FnSubCorpus(final FnLexUnit lu, final SubCorpusType subcorpus)
	{
		this.lu = lu;
		this.subcorpus = subcorpus;
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
		return String.format("[SUBCORPUS id=%s name=%s lu=%s]", this.subcorpus.getName(), this.lu);
	}
}
