package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.Collector;
import org.sqlbuilder.fn.HasId;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.types.FeType;
import org.sqlbuilder.fn.types.GfType;
import org.sqlbuilder.fn.types.PtType;

import java.util.Comparator;
import java.util.Objects;

import edu.berkeley.icsi.framenet.ValenceUnitType;

import static java.util.Comparator.*;

/*
valenceunits.table=fnvalenceunits
valenceunits.create=CREATE TABLE IF NOT EXISTS %Fn_valenceunits.table% ( vuid INTEGER NOT NULL,ferid INTEGER NOT NULL,ptid INTEGER,gfid INTEGER,PRIMARY KEY (vuid) );
valenceunits.altcreate1=ALTER TABLE %Fn_valenceunits.table% ADD COLUMN pt VARCHAR(20) AFTER ptid;
valenceunits.altcreate2=ALTER TABLE %Fn_valenceunits.table% ADD COLUMN gf VARCHAR(10) AFTER gfid;
valenceunits.fk1=ALTER TABLE %Fn_valenceunits.table% ADD CONSTRAINT fk_%Fn_valenceunits.table%_ferid FOREIGN KEY (ferid) REFERENCES %Fn_ferealizations.table% (ferid);
valenceunits.fk2=ALTER TABLE %Fn_valenceunits.table% ADD CONSTRAINT fk_%Fn_valenceunits.table%_ptid FOREIGN KEY (ptid) REFERENCES %Fn_pttypes.table% (ptid);
valenceunits.fk3=ALTER TABLE %Fn_valenceunits.table% ADD CONSTRAINT fk_%Fn_valenceunits.table%_gfid FOREIGN KEY (gfid) REFERENCES %Fn_gftypes.table% (gfid);
valenceunits.no-fk1=ALTER TABLE %Fn_valenceunits.table% DROP CONSTRAINT fk_%Fn_valenceunits.table%_ferid CASCADE;
valenceunits.no-fk2=ALTER TABLE %Fn_valenceunits.table% DROP CONSTRAINT fk_%Fn_valenceunits.table%_gfid CASCADE;
valenceunits.no-fk3=ALTER TABLE %Fn_valenceunits.table% DROP CONSTRAINT fk_%Fn_valenceunits.table%_ptid CASCADE;
valenceunits.insert=INSERT INTO %Fn_valenceunits.table% (vuid,ferid,pt,gf) VALUES(?,?,?,?);
 */
public class ValenceUnit implements HasId, Comparable<ValenceUnit>, Insertable<ValenceUnit>
{
	public static final Comparator<ValenceUnit> COMPARATOR = comparing(ValenceUnit::getFE, nullsFirst(naturalOrder())) //
			.thenComparing(ValenceUnit::getPT, nullsFirst(naturalOrder())) //
			.thenComparing(ValenceUnit::getGF, nullsFirst(naturalOrder()));

	public static final Collector<ValenceUnit> COLLECTOR = new Collector<>(COMPARATOR);

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
	public int compareTo(final ValenceUnit that)
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
		return String.format("%s,%s,%s,%s", //
				getSqlId(), //
				FeType.getSqlId(fe), //
				PtType.getSqlId(pt), //
				GfType.getSqlId(gf));
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s,%s", fe, pt, gf);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("VU fe=%s pt=%s gf=%s", fe, pt, gf);
	}
}
