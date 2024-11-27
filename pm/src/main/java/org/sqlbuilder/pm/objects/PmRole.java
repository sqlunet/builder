package org.sqlbuilder.pm.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;

import java.util.Comparator;
import java.util.Objects;

public class PmRole implements HasId, Insertable, Comparable<PmRole>
{
	public static final Comparator<PmRole> COMPARATOR = Comparator.comparing(PmRole::getPredicate).thenComparing(PmRole::getRole).thenComparing(PmRole::getPos);

	public static final SetCollector<PmRole> COLLECTOR = new SetCollector<>(COMPARATOR);

	public final PmPredicate predicate;

	public final String role;

	public final Character pos;

	// C O N S T R U C T O R

	public static PmRole make(final String predicate, final String role, final Character pos)
	{
		var p = PmPredicate.make(predicate);
		var r = new PmRole(p, role, pos);
		COLLECTOR.add(r);
		return r;
	}

	private PmRole(final PmPredicate predicate, final String role, final Character pos)
	{
		// predicate, role, pos
		this.predicate = predicate;
		this.role = role;
		this.pos = pos;
	}

	public static PmRole parse(final String line) throws ParseException
	{
		// split into fields
		final String[] columns = line.split("\t");
		if (columns.length > PmEntry.SOURCE + 1)
		{
			throw new ParseException("Line has more fields than expected");
		}
		return parse(columns);
	}

	public static PmRole parse(final String[] columns)
	{
		final String predicate = columns[PmEntry.ID_PRED].substring(3);
		final String role = columns[PmEntry.ID_ROLE].substring(3);
		final String pos = columns[PmEntry.ID_POS].substring(3);
		return PmRole.make(predicate, role, pos.charAt(0));
	}

	// A C C E S S

	public PmPredicate getPredicate()
	{
		return predicate;
	}

	public String getRole()
	{
		return role;
	}

	public Character getPos()
	{
		return pos;
	}

	@RequiresIdFrom(type = PmRole.class)
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
		PmRole that = (PmRole) o;
		return predicate.equals(that.predicate) && role.equals(that.role) && pos.equals(that.pos);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(predicate, role, pos);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final PmRole that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%s,'%s','%c'", predicate.getIntId(), role, pos);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("predicate=%s role=%s pos=%s", predicate, role, pos);
	}
}
