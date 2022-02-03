package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

import java.io.Serializable;
import java.util.*;

public class PbRoleSetMember implements Insertable, Comparable<PbRoleSetMember>
{
	public static final Set<PbRoleSetMember> SET = new HashSet<>();

	public static Map<PbRoleSetMember, Integer> MAP;

	final PbRoleSet roleSet;

	final PbWord pbWord;

	public PbRoleSetMember(final PbRoleSet roleSet, final PbWord pbWord)
	{
		this.roleSet = roleSet;
		this.pbWord = pbWord;
		SET.add(this);
	}

	// A C C E S S

	public PbRoleSet getRoleSet()
	{
		return roleSet;
	}

	public PbWord getPbWord()
	{
		return pbWord;
	}

	// O R D E R

	private static final Comparator<PbRoleSetMember> COMPARATOR = Comparator //
			.comparing(PbRoleSetMember::getPbWord) //
			.thenComparing(PbRoleSetMember::getRoleSet);

	@Override
	public int compareTo(final PbRoleSetMember that)
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
