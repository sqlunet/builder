package org.sqlbuilder.pm.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.common.SetCollector;

import java.util.Comparator;
import java.util.Objects;

public class PmPredicate implements HasId, Insertable, Comparable<PmPredicate>
{
	public static final Comparator<PmPredicate> COMPARATOR = Comparator.comparing(PmPredicate::getPredicate);

	public static final SetCollector<PmPredicate> COLLECTOR = new SetCollector<>(COMPARATOR);

	public final String predicate;

	public static PmPredicate make(final String predicate)
	{
		var p = new PmPredicate(predicate);
		COLLECTOR.add(p);
		return p;
	}

	private PmPredicate(final String predicate)
	{
		this.predicate = predicate;
	}

	// A C C E S S

	public String getPredicate()
	{
		return predicate;
	}

	public String getWord()
	{
		return predicate.substring(0, predicate.indexOf('.'));
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
		PmPredicate that = (PmPredicate) o;
		return predicate.equals(that.predicate);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(predicate);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final PmPredicate that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("'%s'", predicate);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[PRED %s]", predicate);
	}
}
