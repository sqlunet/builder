package org.sqlbuilder.sl.objects;

import org.sqlbuilder.common.*;

import java.util.Comparator;

public class Predicate implements HasId, Comparable<Predicate>, Insertable
{
	public static final Comparator<Predicate> COMPARATOR = Comparator.comparing(Predicate::getPredicate);

	public static final SetCollector<Predicate> COLLECTOR = new SetCollector<>(COMPARATOR);

	public final String predicate;

	@SuppressWarnings("UnusedReturnValue")
	public static Predicate make(final String lemma, final String other)
	{
		return new Predicate(lemma);
	}

	private Predicate(final String lemma)
	{
		this.predicate = lemma;
	}
	// A C C E S S

	public String getPredicate()
	{
		return predicate;
	}

	@RequiresIdFrom(type = Predicate.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	@RequiresIdFrom(type = Predicate.class)
	public static Integer getIntId(final Predicate predicate)
	{
		return predicate == null ? null : COLLECTOR.get(predicate);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final Predicate that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("'%s'", predicate);
	}
}
