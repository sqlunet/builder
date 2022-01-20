package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.refs.SemTypeRef;

import java.util.HashSet;
import java.util.Set;

public class FE_SemType extends Pair<Long, SemTypeRef>
{
	public static final Set<FE_SemType> SET = new HashSet<>();

	public FE_SemType(final long feid, final SemTypeRef semtype)
	{
		super(feid, semtype);
	}

	@Override
	public String toString()
	{
		return String.format("[FE-SEM feid=%s semtype=%s]", this.first, this.second);
	}
}
