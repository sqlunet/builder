package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class VnGrouping implements Insertable<VnGrouping>, Comparable<VnGrouping>
{
	protected static final Set<VnGrouping> SET = new HashSet<>();

	public static Map<VnGrouping, Integer> MAP;

	private final String name;

	public VnGrouping(final String name)
	{
		this.name = name;
	}

	public static VnGrouping parse(final String groupingName)
	{
		return new VnGrouping(groupingName);
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
		VnGrouping that = (VnGrouping) o;
		return name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnGrouping that)
	{
		return name.compareTo(that.name);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s", this.name);
	}

	// I N S E R T

	public String dataRow()
	{
		// id
		// name
		return String.format("'%s'", name);
	}
}
