package org.sqlbuilder.fn;


public class FnFrame_CoreFEMember extends Pair<FnFrame, FnFE>
{
	public FnFrame_CoreFEMember(final FnFrame frameid, final FnFE feid)
	{
		super(frameid, feid);
	}

	@Override
	public String toString()
	{
		return String.format("[FR-coreFE frameid=%s feid=%s]", this.first, this.second);
	}
}
