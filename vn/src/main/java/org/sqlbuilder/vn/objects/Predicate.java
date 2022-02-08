package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;

import java.util.*;

public class Predicate implements HasId, Insertable, Comparable<Predicate>
{
	public static final Comparator<Predicate> COMPARATOR = Comparator.comparing(Predicate::getName);

	public static final SetCollector<Predicate> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String name;

	// C O N S T R U C T

	public static Predicate make(final String name)
	{
		var p = new Predicate(name);
		COLLECTOR.add(p);
		return p;
	}

	private Predicate(final String name)
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
		Predicate that = (Predicate) o;
		return name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Predicate that)
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
