package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class RoleSet implements HasId, Insertable, Comparable<RoleSet>
{
	public static final Comparator<RoleSet> COMPARATOR = Comparator //
			.comparing(RoleSet::getPredicate) //
			.thenComparing(RoleSet::getName) //
			//.thenComparing(RoleSet::getAliases)
			;

	public static final SetCollector<RoleSet> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final Predicate predicate;

	private final String name;

	private final String descr;

	private final List<Alias> aliases;

	// C O N S T R U C T O R

	public static RoleSet make(final Predicate predicate, final String roleSetId, final String name, final List<Alias> aliases)
	{
		var s = new RoleSet(predicate, roleSetId, name, aliases);
		COLLECTOR.add(s);
		return s;
	}

	private RoleSet(final Predicate predicate, final String name, final String descr, final List<Alias> aliases)
	{
		this.predicate = predicate;
		this.name = name;
		this.descr = descr;
		this.aliases = aliases;
	}

	// A C C E S S

	public Predicate getPredicate()
	{
		return this.predicate;
	}

	public String getHead()
	{
		return this.predicate.getHead();
	}

	public String getName()
	{
		return this.name;
	}

	public String getDescr()
	{
		return this.descr;
	}

	public List<Alias> getAliases()
	{
		return this.aliases;
	}

	@Override
	public Integer getIntId()
	{
		return null;
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
		RoleSet that = (RoleSet) o;
		return predicate.equals(that.predicate) && name.equals(that.name) && Objects.equals(aliases, that.aliases);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(predicate, name, aliases);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final RoleSet that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = RoleSet.class)
	@RequiresIdFrom(type = PbWord.class)
	@Override
	public String dataRow()
	{
		final Predicate predicate2 = getPredicate();
		final PbWord word = LexItem.map.get(predicate2);

		// Long(1, roleSetId);
		// String(2, getHead());
		// String(3, getName());
		// String(4, getDescr());
		// Long(5, word.getId());

		// (rolesetid),rolesethead,rolesetname,rolesetdescr,pbwordid
		final int roleSetId = RoleSet.COLLECTOR.get(this);
		return String.format("%s,'%s','%s',%s", //
				predicate.getHead(), //
				name, //
				Utils.escape(descr), //
				word == null ? "NULL" : word.getSqlId() //
		);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		if (this.descr == null)
		{
			return String.format("<%s-%s>", getHead(), this.name);
		}
		return String.format("<%s-%s-{%s}>", getHead(), this.name, this.descr);
	}
}
