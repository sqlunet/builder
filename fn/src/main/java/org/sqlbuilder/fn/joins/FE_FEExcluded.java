package org.sqlbuilder.fn.joins;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.InternalFrameRelationFEType;

public class FE_FEExcluded extends Pair<Long, InternalFrameRelationFEType>
{
	public static final Set<FE_FEExcluded> SET = new HashSet<>();

	public FE_FEExcluded(final long feid, final InternalFrameRelationFEType fe2)
	{
		super(feid, fe2);
	}

	@Override
	public String toString()
	{
		return String.format("[FE-FEexcl feid=%s fe2=%s]", this.first, this.second);
	}
}
