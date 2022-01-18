package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

public class FnLexUnit_Governor extends Pair<FnLexUnit, FnGovernor>
{
	public static final Set<FnLexUnit_Governor> SET = new HashSet<>();

	public FnLexUnit_Governor(final FnLexUnit lu, final FnGovernor governor)
	{
		super(lu, governor);
	}

	@Override
	public String toString()
	{
		return String.format("[LU-GOV lu=%s governor=%s]", this.first, this.second);
	}
}
