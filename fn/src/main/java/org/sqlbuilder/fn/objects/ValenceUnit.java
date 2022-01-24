package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;
import org.sqlbuilder.fn.types.FeType;
import org.sqlbuilder.fn.types.GfType;
import org.sqlbuilder.fn.types.PtType;

import java.util.*;

import edu.berkeley.icsi.framenet.ValenceUnitType;

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
public class ValenceUnit implements HasId,Comparable<ValenceUnit>, Insertable<ValenceUnit>
{
	public static final Set<ValenceUnit> SET = new HashSet<>();

	public static Map<ValenceUnit, Integer> MAP = new HashMap<>();

	public final ValenceUnitType vu;

	// C O N S T R U C T O R

	public ValenceUnit(final ValenceUnitType vu)
	{
		this.vu = vu;
		String fe = vu.getFE();
		if (fe.isEmpty())
		{
			FeType.add(vu.getFE());
		}
		String pt = vu.getPT();
		if (pt.isEmpty())
		{
			PtType.add(vu.getPT());
		}
		String gf = vu.getGF();
		if (gf.isEmpty())
		{
			GfType.add(vu.getGF());
		}
		SET.add(this);
	}

	// A C C E S S

	public String getFE()
	{
		return vu.getFE();
	}

	public String getPT()
	{
		return vu.getPT();
	}

	public String getGF()
	{
		return vu.getGF();
	}

	// I D E N T I T Y

	@Override
	public Object getId()
	{
		Integer id = MAP.get(this);
		if (id != null)
		{
			return id;
		}
		return "NULL";
	}

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
		return getFE().equals(that.getFE()) && getPT().equals(that.getPT()) && getGF().equals(that.getGF());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(vu.getFE(), vu.getPT(), vu.getGF());
	}

	// O R D E R

	public static final Comparator<ValenceUnit> COMPARATOR = Comparator.comparing(ValenceUnit::getFE).thenComparing(ValenceUnit::getPT).thenComparing(ValenceUnit::getGF);

	@Override
	public int compareTo(final ValenceUnit that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		String fe = vu.getFE();
		if (fe.isEmpty())
		{
			fe = null;
		}
		String pt = vu.getPT();
		if (pt.isEmpty())
		{
			pt = null;
		}
		String gf = vu.getGF();
		if (gf.isEmpty())
		{
			gf = null;
		}

		//TODO ferid
		return String.format("%s,%s,%s,%s",
				getId(),
				FeType.getId(fe), //
				PtType.getId(pt), //
				GfType.getId(gf));
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s,%s", vu.getFE(), vu.getPT(), vu.getGF());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("VU fe=%s pt=%s gf=%s", this.vu.getFE(), this.vu.getPT(), this.vu.getGF());
	}
}
