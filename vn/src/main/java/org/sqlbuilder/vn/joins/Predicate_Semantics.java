package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.vn.objects.Predicate;
import org.sqlbuilder.vn.objects.Semantics;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Predicate_Semantics implements Insertable, Comparable<Predicate_Semantics>
{
	public static final Set<Predicate_Semantics> SET = new HashSet<>();

	private final Semantics semantics;

	private final Predicate predicate;

	// C O N S T R U C T O R
	public static Predicate_Semantics make(final Predicate predicate, final Semantics semantics)
	{
		var m = new Predicate_Semantics(predicate, semantics);
		SET.add(m);
		return m;
	}

	private Predicate_Semantics(final Predicate predicate, final Semantics semantics)
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
		Predicate_Semantics that = (Predicate_Semantics) o;
		return semantics.equals(that.semantics) && predicate.equals(that.predicate);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(semantics, predicate);
	}

	// O R D E R I N G

	static public final Comparator<Predicate_Semantics> COMPARATOR = Comparator.comparing(Predicate_Semantics::getSemantics).thenComparing(Predicate_Semantics::getPredicate);

	@Override
	public int compareTo(@NotNull final Predicate_Semantics that)
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

	@RequiresIdFrom(type = Predicate.class)
	@RequiresIdFrom(type = Semantics.class)
	@Override
	public String dataRow()
	{
		// predicate.id
		// semantics.id
		return String.format("%d,%d", //
				predicate.getIntId(), //
				semantics.getIntId());
	}

	@Override
	public String comment()
	{
		return String.format("%s", predicate.name);
	}
}
