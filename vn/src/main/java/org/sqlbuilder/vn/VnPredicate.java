package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class VnPredicate implements Insertable<VnPredicate>, Comparable<VnPredicate>
{
	protected static final Set<VnPredicate> SET = new HashSet<>();

	public static Map<VnPredicate, Integer> MAP;

	private final String name;

	// C O N S T R U C T

	public VnPredicate(final String name)
	{
		this.name = name;
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
		VnPredicate that = (VnPredicate) o;
		return name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnPredicate that)
	{
		return this.name.compareTo(that.name);
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
