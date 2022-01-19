package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

public class FnFrame_SemType extends Pair<Long, FnSemTypeRef>
{
	public static final Set<FnFrame_SemType> SET = new HashSet<>();

	public FnFrame_SemType(final long frameid, final FnSemTypeRef semtype)
	{
		super(frameid, semtype);
	}

	@Override
	public String toString()
	{
		return String.format("[FR-SEM frameid=%s semtype=%s]", this.first, this.second);
	}
}
