package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;

import java.util.*;

public class VnPredicate implements Insertable, Comparable<VnPredicate>
{
	public static final Comparator<VnPredicate> COMPARATOR = Comparator.comparing(VnPredicate::getName);

	public static final SetCollector<VnPredicate> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String name;

	// C O N S T R U C T

	public static VnPredicate make(final String name)
	{
		var p = new VnPredicate(name);
		COLLECTOR.add(p);
		return p;
	}

	private VnPredicate(final String name)
	{
		this.name = name;
	}

	// A C C E S S

	public String getName()
	{
		return name;
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
		return COMPARATOR.compare(this, that);
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
