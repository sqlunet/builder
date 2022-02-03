package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;

import java.util.*;

public class VnRoleType implements Insertable, Comparable<VnRoleType>
{
	public static final Comparator<VnRoleType> COMPARATOR = Comparator.comparing(VnRoleType::getType);

	public static final SetCollector<VnRoleType> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String type;

	// C O N S T R U C T

	public static VnRoleType make(final String type)
	{
		var t = new VnRoleType(type);
		COLLECTOR.add(t);
		return t;
	}

	private VnRoleType(final String type)
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
		return COMPARATOR.compare(this, that);
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
