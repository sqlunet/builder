package org.sqlbuilder.fn;

import org.sqlbuilder.fn.joins.Pair;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

public class FnPattern_AnnoSet extends Pair<FnGroupPattern, AnnoSetType>
{
	public static final Set<FnPattern_AnnoSet> SET = new HashSet<>();

	public FnPattern_AnnoSet(final FnGroupPattern pattern, final AnnoSetType annoset)
	{
		super(pattern, annoset);
	}

	@Override
	public String toString()
	{
		return String.format("[PAT-AS pattern=%s annoset=%s]", this.first, this.second);
	}
}
