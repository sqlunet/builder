package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

public class FnPattern_ValenceUnit extends Pair<FnGroupPattern, FnValenceUnitBase> implements Insertable<FnPattern_ValenceUnit>
{
	public static final Set<FnPattern_ValenceUnit> SET = new HashSet<>();

	//public final FnFE fe;

	public FnPattern_ValenceUnit(final FnGroupPattern pattern, final FnValenceUnitBase vu /*, final FnFE fe*/)
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
