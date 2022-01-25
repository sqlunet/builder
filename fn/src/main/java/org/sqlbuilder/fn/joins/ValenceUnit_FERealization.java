package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.FERealization;
import org.sqlbuilder.fn.objects.ValenceUnit;

import java.util.HashSet;
import java.util.Set;

public class ValenceUnit_FERealization extends Pair<ValenceUnit, FERealization> implements Insertable<ValenceUnit_FERealization>
{
	public static final Set<ValenceUnit_FERealization> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static ValenceUnit_FERealization make(final ValenceUnit vu, final FERealization fer)
	{
		var vr = new ValenceUnit_FERealization(vu,fer);
		SET.add(vr);
		return vr;
	}

	private ValenceUnit_FERealization(final ValenceUnit vu, final FERealization fer)
	{
		super(vu, fer);
	}

	// A C C E S S

	public FERealization getFer()
	{
		return second;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%s,%s", //
				first.dataRow(), //
				second.getId());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[VU-FER vu=%s fer=%s]", this.first, this.second);
	}
}
