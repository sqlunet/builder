package org.sqlbuilder.fn.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;
import org.sqlbuilder.fn.types.FeType;
import org.sqlbuilder.fn.types.GfType;
import org.sqlbuilder.fn.types.PtType;

import java.util.Comparator;
import java.util.Objects;

import edu.berkeley.icsi.framenet.ValenceUnitType;

import static java.util.Comparator.*;

public class ValenceUnit implements HasId, Comparable<ValenceUnit>, Insertable
{
	public static final Comparator<ValenceUnit> COMPARATOR = comparing(ValenceUnit::getFE, nullsFirst(naturalOrder())) //
			.thenComparing(ValenceUnit::getPT, nullsFirst(naturalOrder())) //
			.thenComparing(ValenceUnit::getGF, nullsFirst(naturalOrder()));

	public static final SetCollector<ValenceUnit> COLLECTOR = new SetCollector<>(COMPARATOR);

	public final String fe;

	public final String pt;

	public final String gf;

	// C O N S T R U C T O R

	public static ValenceUnit make(final ValenceUnitType vu)
	{
		var v = new ValenceUnit(vu);
		if (v.fe != null)
		{
			FeType.COLLECTOR.add(v.fe);
		}
		if (v.pt != null)
		{
			PtType.COLLECTOR.add(v.pt);
		}
		if (v.gf != null)
		{
			GfType.COLLECTOR.add(v.gf);
		}
		COLLECTOR.add(v);
		return v;
	}

	private ValenceUnit(final ValenceUnitType vu)
	{
		String fe = vu.getFE();
		this.fe = fe == null || fe.isEmpty() ? null : fe;
		String pt = vu.getPT();
		this.pt = pt == null || pt.isEmpty() ? null : pt;
		String gf = vu.getGF();
		this.gf = gf == null || gf.isEmpty() ? null : gf;
	}

	// A C C E S S

	public String getFE()
	{
		return fe;
	}

	public String getPT()
	{
		return pt;
	}

	public String getGF()
	{
		return gf;
	}

	@RequiresIdFrom(type = ValenceUnit.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		ValenceUnit that = (ValenceUnit) o;
		return Objects.equals(fe, that.fe) && Objects.equals(pt, that.pt) && Objects.equals(gf, that.gf);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(fe, pt, gf);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final ValenceUnit that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = ValenceUnit.class)
	@RequiresIdFrom(type = FeType.class)
	@RequiresIdFrom(type = PtType.class)
	@RequiresIdFrom(type = GfType.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%s,%s", //
				FeType.getSqlId(fe), //
				PtType.getSqlId(pt), //
				GfType.getSqlId(gf));
	}

	@Override
	public String comment()
	{
		return String.format("fe=%s,pt=%s,gf=%s", fe, pt, gf);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("VU fe=%s pt=%s gf=%s", fe, pt, gf);
	}
}
