package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.Insertable;

import java.util.*;

public class RoleSetMember implements Insertable, Comparable<RoleSetMember>
{
	public static final Set<RoleSetMember> SET = new HashSet<>();

	public static Map<RoleSetMember, Integer> MAP;

	final RoleSet roleSet;

	final PbWord pbWord;

	public RoleSetMember(final RoleSet roleSet, final PbWord pbWord)
	{
		this.roleSet = roleSet;
		this.pbWord = pbWord;
		SET.add(this);
	}

	// A C C E S S

	public RoleSet getRoleSet()
	{
		return roleSet;
	}

	public PbWord getPbWord()
	{
		return pbWord;
	}

	// O R D E R

	private static final Comparator<RoleSetMember> COMPARATOR = Comparator //
			.comparing(RoleSetMember::getPbWord) //
			.thenComparing(RoleSetMember::getRoleSet);

	@Override
	public int compareTo(final RoleSetMember that)
	{
		return COMPARATOR.compare(this, that);
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.roleSetId);
		// Long(2, this.pbWordId);
		return null;
	}
}
