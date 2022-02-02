package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.AnnotationSet;
import org.sqlbuilder.fn.objects.ValenceUnit;

import java.util.HashSet;
import java.util.Set;

public class ValenceUnit_AnnoSet extends Pair<ValenceUnit, Integer> implements Insertable<ValenceUnit_AnnoSet>
{
	public static final Set<ValenceUnit_AnnoSet> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static ValenceUnit_AnnoSet make(final ValenceUnit vu, final int annosetid)
	{
		var va = new ValenceUnit_AnnoSet(vu, annosetid);
		SET.add(va);
		return va;
	}

	private ValenceUnit_AnnoSet(final ValenceUnit vu, final int annosetid)
	{
		super(vu, annosetid);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%s,%s", //
				first.getIntId(), //
				second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[VU-AS vu=%s annoset=%s]", first, second);
	}
}
