package org.sqlbuilder.sl.foreign;

import java.util.Comparator;

public class PbRole implements Comparable<PbRole>
{
	static final Comparator<PbRole> COMPARATOR = Comparator.comparing(PbRole::getRoleSet).thenComparing(PbRole::getArg);

	public final String roleSet;

	public final String arg;

	public static PbRole make(final String roleSet, final String arg)
	{
		return new PbRole(roleSet, arg);
	}

	private PbRole(final String roleSet, final String arg)
	{
		this.roleSet = roleSet;
		this.arg = arg;
	}

	public String getRoleSet()
	{
		return roleSet;
	}

	public String getArg()
	{
		return arg;
	}

	@Override
	public int compareTo(final PbRole that)
	{
		return COMPARATOR.compare(this, that);
	}
}
