package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.refs.SemTypeRef;

import java.util.HashSet;
import java.util.Set;

public class Frame_SemType extends Pair<Long, SemTypeRef>
{
	public static final Set<Frame_SemType> SET = new HashSet<>();

	public Frame_SemType(final long frameid, final SemTypeRef semtype)
	{
		super(frameid, semtype);
	}

	@Override
	public String toString()
	{
		return String.format("[FR-SEM frameid=%s semtype=%s]", this.first, this.second);
	}
}
