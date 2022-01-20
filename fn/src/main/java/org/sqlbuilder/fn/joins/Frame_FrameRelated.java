package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FrameIDNameType;

public class Frame_FrameRelated extends Pair<Long, FrameIDNameType> implements Insertable<Frame_FrameRelated>
{
	public static final Set<Frame_FrameRelated> SET = new HashSet<>();

	private final String relation;

	public Frame_FrameRelated(final long frameid, final FrameIDNameType frame2, final String relation)
	{
		super(frameid, frame2);
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
