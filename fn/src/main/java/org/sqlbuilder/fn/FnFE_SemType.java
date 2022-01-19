package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

public class FnFE_SemType extends Pair<Long, FnSemTypeRef>
{
	public static final Set<FnFE_SemType> SET = new HashSet<>();

	public FnFE_SemType(final long feid, final FnSemTypeRef semtype)
	{
		super(feid, semtype);
	}

	@Override
	public String toString()
	{
		return String.format("[FE-SEM feid=%s semtype=%s]", this.first, this.second);
	}
}
