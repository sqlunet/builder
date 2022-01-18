package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

public class FnPos extends FnName
{
	public static final Set<FnPos> SET = new HashSet<>();

	public FnPos(final String pos)
	{
		super(pos);
	}
}
