package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.*;

public class VnClass implements Insertable, Comparable<VnClass>
{
	protected static final Set<VnClass> SET = new HashSet<>();

	public static Map<VnClass, Integer> MAP;

	private final String name;

	public VnClass(final String name)
	{
		this.name = name;
	}

	// A C C E S S

	public String getTag()
	{
		final int split = this.name.indexOf('-');
		return this.name.substring(split + 1);
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
		VnClass that = (VnClass) o;
		return name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnClass that)
	{
		return name.compareTo(that.name);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return this.name;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// id
		// name
		// tag
		return String.format("'%s','%s'", name, getTag());
	}
}
