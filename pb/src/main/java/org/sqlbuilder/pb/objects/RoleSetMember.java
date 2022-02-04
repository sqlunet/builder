package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class RoleSetMember implements Insertable, Comparable<RoleSetMember>
{
	public static final Comparator<RoleSetMember> COMPARATOR = Comparator //
			.comparing(RoleSetMember::getWord) //
			.thenComparing(RoleSetMember::getRoleSet);

	public static final Set<RoleSetMember> SET = new HashSet<>();

	final RoleSet roleSet;

	final PbWord word;

	// C O N S T R U C T O R

	public static RoleSetMember make(final RoleSet roleSet, final PbWord pbWord)
	{
		var m = new RoleSetMember(roleSet, pbWord);
		SET.add(m);
		return m;
	}

	private RoleSetMember(final RoleSet roleSet, final PbWord word)
	{
		this.roleSet = roleSet;
		this.word = word;
		SET.add(this);
	}

	// A C C E S S

	public RoleSet getRoleSet()
	{
		return roleSet;
	}

	public PbWord getWord()
	{
		return word;
	}

	// O R D E R

	@Override
	public int compareTo(final RoleSetMember that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = RoleSet.class)
	@RequiresIdFrom(type = PbWord.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%s", roleSet.getIntId(), word.getIntId());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", roleSet.getName(), word.getWord());
	}
}
