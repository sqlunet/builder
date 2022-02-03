package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.vn.objects.Predicate;
import org.sqlbuilder.vn.objects.Semantics;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class VnPredicateMapping implements Insertable, Comparable<VnPredicateMapping>
{
	public static final Set<VnPredicateMapping> SET = new HashSet<>();

	private final Semantics semantics;

	private final Predicate predicate;

	// C O N S T R U C T

	public VnPredicateMapping(final Semantics semantics, final Predicate predicate)
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

	public Semantics getSemantics()
	{
		return semantics;
	}

	public Predicate getPredicate()
	{
		return predicate;
	}

	// I N S E R T

	@RequiresIdFrom(type = Semantics.class)
	@RequiresIdFrom(type = Predicate.class)
	@Override
	public String dataRow()
	{
		// semantics.id
		// predicate.id
		return String.format("%d,%d", Semantics.COLLECTOR.get(semantics), Predicate.COLLECTOR.get(predicate));
	}
}
