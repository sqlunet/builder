package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.FnValenceUnit;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

public class FnValenceUnit_AnnoSet extends Pair<FnValenceUnit, AnnoSetType>
{
	public static final Set<FnValenceUnit_AnnoSet> SET = new HashSet<>();

	public FnValenceUnit_AnnoSet(final FnValenceUnit vu, final AnnoSetType annoset)
	{
		super(vu, annoset);
	}

	@Override
	public String toString()
	{
		return String.format("[VU-AS vu=%s annoset=%s]", this.first, this.second);
	}
}
