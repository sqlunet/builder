package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEType;
import edu.berkeley.icsi.framenet.InternalFrameRelationFEType;

public class FnFERequired extends Pair<Long, InternalFrameRelationFEType>
{
	public static final Set<FnFERequired> SET = new HashSet<>();

	public FnFERequired(final long fe, final InternalFrameRelationFEType fe2)
	{
		super(fe, fe2);
	}

	@Override
	public String toString()
	{
		return String.format("[FEreq feid=%s fe2=%s]", this.first, this.second);
	}
}
