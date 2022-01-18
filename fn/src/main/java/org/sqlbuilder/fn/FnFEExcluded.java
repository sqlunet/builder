package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEType;
import edu.berkeley.icsi.framenet.InternalFrameRelationFEType;

public class FnFEExcluded extends Pair<FEType, InternalFrameRelationFEType>
{
	public static final Set<FnFEExcluded> SET = new HashSet<>();

	public FnFEExcluded(final FEType fe1, final InternalFrameRelationFEType fe2)
	{
		super(fe1, fe2);
	}

	@Override
	public String toString()
	{
		return String.format("[FEexcl fe1=%s fe2=%s]", this.first, this.second);
	}
}
