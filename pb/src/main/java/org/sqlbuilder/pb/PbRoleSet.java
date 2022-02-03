package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

import java.util.*;

public class PbRoleSet implements Insertable, Comparable<PbRoleSet>
{
	public static final Set<PbRoleSet> SET = new HashSet<>();

	protected static Map<PbRoleSet, Integer> MAP;

	private final PbPredicate predicate;

	private final String name;

	private final String descr;

	private final List<PbAlias> aliases;

	// C O N S T R U C T O R

	public PbRoleSet(final PbPredicate predicate, final String name, final String descr, final List<PbAlias> aliases)
	{
		this.predicate = predicate;
		this.name = name;
		this.descr = descr;
		this.aliases = aliases;
	}

	public static PbRoleSet make(final PbPredicate predicate, final String roleSetId, final String name, final List<PbAlias> aliases)
	{
		return new PbRoleSet(predicate, roleSetId, name, aliases);
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
		if (!(rs instanceof PbRoleSet))
		{
			return false;
		}
		return compareTo((PbRoleSet) rs) == 0;
	}

	// O R D E R I N G

	@Override
	public int compareTo(final PbRoleSet rs)
	{
		final int c = this.predicate.getHead().compareTo(rs.getHead());
		if (c != 0)
		{
			return c;
		}
		return this.name.compareTo(rs.name);
	}

	// A C C E S S

	public PbPredicate getPredicate()
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

	public List<PbAlias> getAliases()
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
