package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.common.SetCollector;

import java.util.Comparator;
import java.util.Objects;

public class Grouping implements HasId, Insertable, Comparable<Grouping>
{
	public static final Comparator<Grouping> COMPARATOR = Comparator.comparing(Grouping::getName);

	public static final SetCollector<Grouping> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String name;

	public static Grouping make(final String groupingName)
	{
		var g = new Grouping(groupingName);
		COLLECTOR.add(g);
		return g;
	}

	private Grouping(final String name)
	{
		this.name = name;
	}

	// A C C E S S

	public String getName()
	{
		return name;
	}

	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
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
		Grouping that = (Grouping) o;
		return name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}

	// O R D E R I N G

	@Override
	public int compareTo(@NotNull final Grouping that)
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
