package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;

import java.util.Comparator;

public class VnFrameName implements Insertable, Comparable<VnFrameName>
{
	public static final Comparator<VnFrameName> COMPARATOR = Comparator.comparing(VnFrameName::getName);

	public static final SetCollector<VnFrameName> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String name;

	// C O N S T R U C T

	public static VnFrameName make(final String name)
	{
		var n = new VnFrameName(name);
		COLLECTOR.add(n);
		return n;
	}

	private VnFrameName(final String name)
	{
		this.name = name.trim().toUpperCase().replaceAll("\\s+", " ");
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
