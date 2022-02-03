package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.Insertable;

import java.util.*;

public class RoleSet implements Insertable, Comparable<RoleSet>
{
	public static final Set<RoleSet> SET = new HashSet<>();

	public static Map<RoleSet, Integer> MAP;

	private final Predicate predicate;

	private final String name;

	private final String descr;

	private final List<Alias> aliases;

	// C O N S T R U C T O R

	public RoleSet(final Predicate predicate, final String name, final String descr, final List<Alias> aliases)
	{
		this.predicate = predicate;
		this.name = name;
		this.descr = descr;
		this.aliases = aliases;
	}

	public static RoleSet make(final Predicate predicate, final String roleSetId, final String name, final List<Alias> aliases)
	{
		return new RoleSet(predicate, roleSetId, name, aliases);
	}

	// I D E N T I T Y

	@Override
	public int hashCode()
	{
		return 7 * this.predicate.getHead().hashCode() + 17 * this.name.hashCode();
	}

	@Override
	public boolean equals(final Object rs)
	{
		if (!(rs instanceof RoleSet))
		{
			return false;
		}
		return compareTo((RoleSet) rs) == 0;
	}

	// O R D E R I N G

	@Override
	public int compareTo(final RoleSet rs)
	{
		final int c = this.predicate.getHead().compareTo(rs.getHead());
		if (c != 0)
		{
			return c;
		}
		return this.name.compareTo(rs.name);
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

	// I N S E R T

	@Override
	public String dataRow()
	{
		// final long roleSetId = PbRoleSet.map.getId(this);
		// final PbPredicate predicate2 = getPredicate();
		// final PbWord word = PbLexItem.map.get(predicate2);

		// rolesetid,head,name,desc,wordid
		// Long(1, roleSetId);
		// String(2, getHead());
		// String(3, getName());
		// String(4, getDescr());
		// Long(5, word.getId());

		return null;
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
