package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.objects.ValenceUnit;
import org.sqlbuilder.fn.objects.FERealization;

import java.util.HashSet;
import java.util.Set;

public class ValenceUnit_FERealization extends Pair<ValenceUnit, FERealization> implements Insertable<ValenceUnit_FERealization>
{
	public static final Set<ValenceUnit_FERealization> SET = new HashSet<>();

	// C O N S T R U C T O R

	public ValenceUnit_FERealization(final ValenceUnit vu, final FERealization fer)
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
		String pt = first.vu.getPT();
		if (pt.isEmpty())
		{
			pt = null;
		}
		String gf = first.vu.getGF();
		if (gf.isEmpty())
		{
			gf = null;
		}

		return String.format("%s,", //
				"NULL", // getId()
				Utils.nullableString(pt), //
				Utils.nullableString(gf), //
				"NULL"); // fer.getId()
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[VU fe=%s pt=%s gf=%s fer=%s]", this.first.vu.getFE(), this.first.vu.getPT(), this.first.vu.getGF(), this.second);
	}
}
