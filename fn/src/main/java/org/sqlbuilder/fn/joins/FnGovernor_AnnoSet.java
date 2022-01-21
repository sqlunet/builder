package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.objects.Governor;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

public class FnGovernor_AnnoSet extends Pair<Governor, AnnoSetType>
{
	public static final Set<FnGovernor_AnnoSet> SET = new HashSet<>();

	public FnGovernor_AnnoSet(final Governor governor, final AnnoSetType annoset)
	{
		super(governor, annoset);
	}

	@Override
	public String toString()
	{
		return String.format("[GOV-AS governor=%s annoset=%s]", this.first, this.second);
	}
}
