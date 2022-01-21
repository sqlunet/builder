package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.objects.ValenceUnit;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

public class ValenceUnit_AnnoSet extends Pair<ValenceUnit, AnnoSetType>
{
	public static final Set<ValenceUnit_AnnoSet> SET = new HashSet<>();

	public ValenceUnit_AnnoSet(final ValenceUnit vu, final AnnoSetType annoset)
	{
		super(vu, annoset);
	}

	@Override
	public String toString()
	{
		return String.format("[VU-AS vu=%s annoset=%s]", this.first, this.second);
	}
}
