package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.common.SetCollector2;

import java.util.Comparator;
import java.util.Objects;

public class VnClass implements HasId, Insertable, Comparable<VnClass>
{
	public static final Comparator<VnClass> COMPARATOR = Comparator.comparing(VnClass::getName);

	public static final SetCollector2<VnClass> COLLECTOR = new SetCollector2<>(COMPARATOR);

	private final String name;

	public static VnClass make(final String name)
	{
		if (name == null || name.isEmpty())
		{
			throw new RuntimeException("No name");
		}

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

	@Override
	public Integer getIntId()
	{
		return COLLECTOR.apply(this);
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
	public int compareTo(@NotNull final VnClass that)
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
