package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.ValenceUnitType;

public class FnValenceUnit extends FnValenceUnitBase implements Insertable<FnValenceUnit>
{
	public static final Set<FnValenceUnit> SET = new HashSet<>();

	// M E M B E R S

	public final FnFERealization fer;

	// C O N S T R U C T O R

	public FnValenceUnit(final FnFERealization fer, final ValenceUnitType vu)
	{
		super(vu);
		this.fer = fer;
		FnValenceUnitBase.SET.add(this);
	}

	// I D E N T I T Y

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return super.equals(obj);
	}

	// A C C E S S

	public FnFERealization getFer()
	{
		return fer;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		String pt = this.vu.getPT();
		if (pt.isEmpty())
		{
			pt = null;
		}
		String gf = this.vu.getGF();
		if (gf.isEmpty())
		{
			gf = null;
		}

		//Long(1, getId());
		//Long(2, this.ferid);
		//String(3, pt);
		//Null(3, Types.VARCHAR);
		//String(4, gf);
		//Null(4, Types.VARCHAR);
		return null;
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[VU fer=%s fe=%s pt=%s gf=%s]", this.fer, this.vu.getFE(), this.vu.getPT(), this.vu.getGF());
	}
}
