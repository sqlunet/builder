package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;

import java.util.Comparator;
import java.util.Objects;

public class VnClass implements Insertable, Comparable<VnClass>
{
	public static final Comparator<VnClass> COMPARATOR = Comparator.comparing(VnClass::getName);

	public static final SetCollector<VnClass> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String name;

	public static VnClass make(final String name)
	{
		var c = new VnClass(name);
		COLLECTOR.add(c);
		return c;
	}

	private VnClass(final String name)
	{
		this.name = name;
	}

	// A C C E S S

	public String getName()
	{
		return name;
	}

	public String getTag()
	{
		final int split = this.name.indexOf('-');
		return this.name.substring(split + 1);
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
		VnClass that = (VnClass) o;
		return name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnClass that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

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
		// tag
		return String.format("'%s','%s'", name, getTag());
	}
}
