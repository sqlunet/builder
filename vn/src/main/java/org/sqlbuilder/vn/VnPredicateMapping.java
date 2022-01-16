package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.*;

public class VnPredicateMapping implements Insertable<VnPredicateMapping>, Comparable<VnPredicateMapping>
{
	protected static final SortedSet<VnPredicateMapping> SET = new TreeSet<>();

	private final VnSemantics semantics;

	private final VnPredicate predicate;

	// C O N S T R U C T

	public VnPredicateMapping(final VnSemantics semantics, final VnPredicate predicate)
	{
		this.semantics = semantics;
		this.predicate = predicate;
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
		VnPredicateMapping that = (VnPredicateMapping) o;
		return semantics.equals(that.semantics) && predicate.equals(that.predicate);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(semantics, predicate);
	}

	// O R D E R I N G

	static public final Comparator<VnPredicateMapping> COMPARATOR = Comparator.comparing(VnPredicateMapping::getSemantics).thenComparing(VnPredicateMapping::getPredicate);

	@Override
	public int compareTo(final VnPredicateMapping that)
	{
		return COMPARATOR.compare(this, that);
	}

	// A C C E S S

	public VnSemantics getSemantics()
	{
		return semantics;
	}

	public VnPredicate getPredicate()
	{
		return predicate;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// semantics.id
		// predicate.id
		return String.format("%d,%d", VnSemantics.MAP.get(semantics), VnPredicate.MAP.get(predicate));
	}
}
