package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public class VnRoleType implements Insertable<VnRoleType>, Comparable<VnRoleType>
{
	protected static final SortedSet<VnRoleType> SET = new TreeSet<>();

	public static Map<VnRoleType, Integer> MAP;

	private final String type;

	// C O N S T R U C T

	VnRoleType(final String type)
	{
		this.type = type;
	}

	// A C C E S S

	public String getType()
	{
		return this.type;
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
		VnRoleType that = (VnRoleType) o;
		return type.equals(that.type);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnRoleType that)
	{
		return this.type.compareTo(that.type);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return this.type;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// id
		// type
		return String.format("'%s'", type);
	}
}
