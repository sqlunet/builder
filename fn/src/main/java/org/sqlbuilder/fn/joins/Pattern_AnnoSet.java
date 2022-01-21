package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.objects.Pattern;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

public class Pattern_AnnoSet extends Pair<Pattern, AnnoSetType>
{
	public static final Set<Pattern_AnnoSet> SET = new HashSet<>();

	public Pattern_AnnoSet(final Pattern pattern, final AnnoSetType annoset)
	{
		super(pattern, annoset);
	}

	@Override
	public String toString()
	{
		return String.format("[PAT-AS pattern=%s annoset=%s]", this.first, this.second);
	}
}
