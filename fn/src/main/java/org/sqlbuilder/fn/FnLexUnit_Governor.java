package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

public class FnLexUnit_Governor extends Pair<Long, FnGovernor>
{
	public static final Set<FnLexUnit_Governor> SET = new HashSet<>();

	public FnLexUnit_Governor(final long luid, final FnGovernor governor)
	{
		super(luid, governor);
	}

	@Override
	public String toString()
	{
		return String.format("[LU-GOV lu=%s governor=%s]", this.first, this.second);
	}
}
