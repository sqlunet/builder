package org.sqlbuilder.fn;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.berkeley.icsi.framenet.ValenceUnitType;

public class FnValenceUnitBase
{
	protected static final Map<FnValenceUnitBase, Long> map = new HashMap<>();

	public final ValenceUnitType vu;

	// C O N S T R U C T O R

	public FnValenceUnitBase(final ValenceUnitType vu)
	{
		this.vu = vu;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof FnValenceUnitBase))
			return false;
		final FnValenceUnitBase vu2 = (FnValenceUnitBase) o;
		return this.vu.getFE().equals(vu2.vu.getFE()) && this.vu.getPT().equals(vu2.vu.getPT()) && this.vu.getGF().equals(vu2.vu.getGF());
	}

	@Override
	public int hashCode()
	{
		return this.vu.getFE().hashCode() + 7 * this.vu.getPT().hashCode() + 13 * this.vu.getGF().hashCode();
	}

	// F I N D

	public Long findId()
	{
		return FnValenceUnitBase.map.get(this);
	}

	public static void clearMap()
	{
		FnValenceUnitBase.map.clear();
	}

	public static void dumpMap()
	{
		for (final Entry<FnValenceUnitBase, Long> e : FnValenceUnitBase.map.entrySet())
		{
			System.err.println("\t" + e.getKey() + " - " + e.getValue());
		}
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("VUb fe=%s pt=%s gf=%s", this.vu.getFE(), this.vu.getPT(), this.vu.getGF());
	}
}
