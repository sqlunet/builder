package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FrameIDNameType;

public class FnFrameRelated extends Pair<FnFrame, FrameIDNameType> implements Insertable<FnFrameRelated>
{
	public static final Set<FnFrameRelated> SET = new HashSet<>();

	private final String relation;

	public FnFrameRelated(final FnFrame frame1, final FrameIDNameType frame2, final String relation)
	{
		super(frame1, frame2);
		this.relation = relation;
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.frameid);
		// String(2, this.frame2);
		// String(3, this.relation);
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[FRrel frameid=%s frame2=%s type=%s]", this.first, this.second, this.relation);
	}
}
