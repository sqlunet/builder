package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

public class FnCoreType extends FnName
{
	public static final Set<FnCoreType> SET = new HashSet<>();

	public FnCoreType(final String coretype)
	{
		super(coretype);
	}
}
