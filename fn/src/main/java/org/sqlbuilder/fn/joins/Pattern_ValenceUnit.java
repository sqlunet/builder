package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.Pattern;
import org.sqlbuilder.fn.objects.ValenceUnit;

import java.util.HashSet;
import java.util.Set;

public class Pattern_ValenceUnit extends Pair<Pattern, ValenceUnit> implements Insertable<Pattern_ValenceUnit>
{
	public static final Set<Pattern_ValenceUnit> SET = new HashSet<>();

	//public final FnFE fe;

	public Pattern_ValenceUnit(final Pattern pattern, final ValenceUnit vu /*, final FnFE fe*/)
	{
		super(pattern, vu);
		//this.fe = fe;
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.patternid);
		// Long(2, this.vuid);
		// String(3, this.fe);
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[PAT-VU pattern=%s vu=%s fe=%s]", this.first, this.second, null /*this.fe*/);
	}
}
