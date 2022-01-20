package org.sqlbuilder.fn.joins;


import org.sqlbuilder.fn.objects.FE;
import org.sqlbuilder.fn.objects.Frame;

public class Frame_CoreFEMember extends Pair<Frame, FE>
{
	public Frame_CoreFEMember(final Frame frameid, final FE feid)
	{
		super(frameid, feid);
	}

	@Override
	public String toString()
	{
		return String.format("[FR-coreFE frameid=%s feid=%s]", this.first, this.second);
	}
}
