package org.sqlbuilder.fn.joins;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.InternalFrameRelationFEType;

public class FE_FERequired extends Pair<Long, InternalFrameRelationFEType>
{
	public static final Set<FE_FERequired> SET = new HashSet<>();

	public FE_FERequired(final long fe, final InternalFrameRelationFEType fe2)
	{
		super(fe, fe2);
	}

	@Override
	public String toString()
	{
		return String.format("[FE-FEreq feid=%s fe2=%s]", this.first, this.second);
	}
}
