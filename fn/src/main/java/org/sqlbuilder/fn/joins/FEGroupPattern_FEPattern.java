package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.fn.objects.FERealization;
import org.sqlbuilder.fn.objects.ValenceUnit;

import java.util.HashSet;
import java.util.Set;

public class FEGroupPattern_FEPattern extends Triple<FEGroupPattern, FERealization, ValenceUnit> implements Insertable
{
	public static final Set<FEGroupPattern_FEPattern> SET = new HashSet<>();

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static FEGroupPattern_FEPattern make(final FEGroupPattern groupPattern, final FERealization fer, final ValenceUnit vu)
	{
		var p = new FEGroupPattern_FEPattern(groupPattern, fer, vu);
		SET.add(p);
		return p;
	}

	private FEGroupPattern_FEPattern(final FEGroupPattern groupPattern, final FERealization fer, final ValenceUnit vu)
	{
		super(groupPattern, fer, vu);
	}

	// I N S E R T

	@RequiresIdFrom(type = FEGroupPattern.class)
	@RequiresIdFrom(type = FERealization.class)
	@RequiresIdFrom(type = ValenceUnit.class)
	@Override
	// grouppatternid, ferid, vuid
	public String dataRow()
	{
		return String.format("%s,%s,%s", first.getSqlId(), second.getSqlId(), third.getSqlId());
	}

	@Override
	public String comment()
	{
		return String.format("fegr={%s} fer={%s} vu={%s}", first.comment(), second.comment(), third.comment());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[PAT-VU fer=%s pattern=%s, vu=%s]", second, first, first);
	}
}
