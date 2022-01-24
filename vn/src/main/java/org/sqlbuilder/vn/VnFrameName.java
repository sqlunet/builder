package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VnFrameName implements Insertable<VnFrameName>, Comparable<VnFrameName>
{
	protected static final Set<VnFrameName> SET = new HashSet<>();

	public static Map<VnFrameName, Integer> MAP;

	private final String name;

	// C O N S T R U C T

	public VnFrameName(final String name)
	{
		this.name = name.trim().toUpperCase().replaceAll("\\s+", " ");
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof VnFrameName))
		{
			return false;
		}
		final VnFrameName that = (VnFrameName) o;
		return this.name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnFrameName that)
	{
		return this.name.compareTo(that.name);
	}

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
		return String.format("'%s'", name);
	}
}
