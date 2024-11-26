package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.common.SetCollector2;

import java.util.Comparator;

public class FrameName implements HasId, Insertable, Comparable<FrameName>
{
	public static final Comparator<FrameName> COMPARATOR = Comparator.comparing(FrameName::getName);

	public static final SetCollector2<FrameName> COLLECTOR = new SetCollector2<>(COMPARATOR);

	private final String name;

	// C O N S T R U C T O R

	public static FrameName make(final String name)
	{
		var n = new FrameName(name);
		COLLECTOR.add(n);
		return n;
	}

	private FrameName(final String name)
	{
		this.name = name.trim().toUpperCase().replaceAll("\\s+", " ");
	}

	// A C C E S S

	public String getName()
	{
		return name;
	}

	@Override
	public Integer getIntId()
	{
		return COLLECTOR.apply(this);
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof FrameName))
		{
			return false;
		}
		final FrameName that = (FrameName) o;
		return this.name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}

	// O R D E R I N G

	@Override
	public int compareTo(@NotNull final FrameName that)
	{
		return COMPARATOR.compare(this, that);
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
