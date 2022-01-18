package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

public class FnFE_SemType extends Pair<FnFE, FnSemTypeRef>
{
	public static final Set<FnFE_SemType> SET = new HashSet<>();

	public FnFE_SemType(final FnFE fe, final FnSemTypeRef semtype)
	{
		super(fe, semtype);
	}

	@Override
	public String toString()
	{
		return String.format("[FE-SEM fe=%s semtype=%s]", this.first, this.second);
	}
}
