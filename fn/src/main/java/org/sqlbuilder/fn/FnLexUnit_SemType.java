package org.sqlbuilder.fn;


import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeRefType;

public class FnLexUnit_SemType extends Pair<Long, SemTypeRefType>
{
	public static final Set<FnLexUnit_SemType> SET = new HashSet<>();

	public FnLexUnit_SemType(final long luid, final SemTypeRefType semtype)
	{
		super(luid, semtype);
	}

	@Override
	public String toString()
	{
		return String.format("[LU-SEM luid=%s semtype=%s]", this.first, this.second);
	}
}
