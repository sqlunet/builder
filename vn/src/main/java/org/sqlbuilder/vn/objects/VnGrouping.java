package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;

import java.util.Comparator;
import java.util.Objects;

public class VnGrouping implements Insertable, Comparable<VnGrouping>
{
	public static final Comparator<VnGrouping> COMPARATOR = Comparator.comparing(VnGrouping::getName);

	public static final SetCollector<VnGrouping> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String name;

	public static VnGrouping make(final String groupingName)
	{
		var g = new VnGrouping(groupingName);
		COLLECTOR.add(g);
		return g;
	}

	private VnGrouping(final String name)
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
		return COMPARATOR.compare(this, that);
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
