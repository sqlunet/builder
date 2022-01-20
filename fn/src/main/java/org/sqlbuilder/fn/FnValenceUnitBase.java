package org.sqlbuilder.fn;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.berkeley.icsi.framenet.ValenceUnitType;

public class FnValenceUnitBase implements Comparable<FnValenceUnitBase>
{
	public static final SortedSet<FnValenceUnitBase> SET = new TreeSet<>();

	public static Map<FnValenceUnitBase, Long> MAP = new HashMap<>();

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
		{
			return false;
		}
		final FnValenceUnitBase vu2 = (FnValenceUnitBase) o;
		return this.vu.getFE().equals(vu2.vu.getFE()) && this.vu.getPT().equals(vu2.vu.getPT()) && this.vu.getGF().equals(vu2.vu.getGF());
	}

	@Override
	public int hashCode()
	{
		return this.vu.getFE().hashCode() + 7 * this.vu.getPT().hashCode() + 13 * this.vu.getGF().hashCode();
	}

	// A C C E S S

	public ValenceUnitType getVu()
	{
		return vu;
	}

	// O R D E R

	@Override
	public int compareTo(final FnValenceUnitBase o)
	{
		return 0;
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("VUb fe=%s pt=%s gf=%s", this.vu.getFE(), this.vu.getPT(), this.vu.getGF());
	}
}
