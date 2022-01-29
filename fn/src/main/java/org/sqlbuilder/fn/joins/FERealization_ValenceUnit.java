package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.objects.FERealization;
import org.sqlbuilder.fn.objects.ValenceUnit;

import java.util.HashSet;
import java.util.Set;

public class FERealization_ValenceUnit extends Pair<ValenceUnit, FERealization> implements Insertable<FERealization_ValenceUnit>
{
	public static final Set<FERealization_ValenceUnit> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static FERealization_ValenceUnit make(final FERealization fer, final ValenceUnit vu)
	{
		var vr = new FERealization_ValenceUnit(vu,fer);
		SET.add(vr);
		return vr;
	}

	private FERealization_ValenceUnit(final ValenceUnit vu, final FERealization fer)
	{
		super(vu, fer);
	}

	// A C C E S S

	public FERealization getFer()
	{
		return second;
	}

	// I N S E R T

	@RequiresIdFrom(type = FERealization.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%s", //
				first.dataRow(), //
				second.getSqlId());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[VU-FER vu=%s fer=%s]", this.first, this.second);
	}
}
