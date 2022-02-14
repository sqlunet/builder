package org.sqlbuilder.pm;

public class PmResolvingRole extends PmRole
{
	public int pmroleid;

	public PmResolvingRole(final String predicate, final String role, final String pos)
	{
		super(predicate, role, pos);
	}

	@Override
	public String toString()
	{
		return String.format("%s pmroleid=%d", super.toString(), pmroleid);
	}
}
