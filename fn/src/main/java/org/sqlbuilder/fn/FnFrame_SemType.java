package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

public class FnFrame_SemType extends Pair<FnFrame, FnSemTypeRef>
{
	public static final Set<FnFrame_SemType> SET = new HashSet<>();

	public FnFrame_SemType(final FnFrame frame, final FnSemTypeRef semtype)
	{
		super(frame, semtype);
	}

	@Override
	public String toString()
	{
		return String.format("[FR-SEM frame=%s semtype=%s]", this.first, this.second);
	}
}
