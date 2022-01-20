package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.FnGovernor;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

public class FnGovernor_AnnoSet extends Pair<FnGovernor, AnnoSetType>
{
	public static final Set<FnGovernor_AnnoSet> SET = new HashSet<>();

	public FnGovernor_AnnoSet(final FnGovernor governor, final AnnoSetType annoset)
	{
		super(governor, annoset);
	}

	@Override
	public String toString()
	{
		return String.format("[GOV-AS governor=%s annoset=%s]", this.first, this.second);
	}
}
