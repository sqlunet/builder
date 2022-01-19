package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEType;
import edu.berkeley.icsi.framenet.InternalFrameRelationFEType;

public class FnFEExcluded extends Pair<Long, InternalFrameRelationFEType>
{
	public static final Set<FnFEExcluded> SET = new HashSet<>();

	public FnFEExcluded(final long feid, final InternalFrameRelationFEType fe2)
	{
		super(feid, fe2);
	}

	@Override
	public String toString()
	{
		return String.format("[FEexcl feid=%s fe2=%s]", this.first, this.second);
	}
}
