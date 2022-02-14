package org.sqlbuilder.pm;

import org.sqlbuilder.common.Insertable;

public class PmRole implements Insertable
{
	public final String predicate;

	public final String role;

	public final String pos;

	public PmRole(final String predicate, final String role, final String pos)
	{
		// predicate, role, pos
		this.predicate = predicate;
		this.role = role;
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return String.format("predicate=%s role=%s pos=%s", predicate, role, pos);
	}

	@Override
	public String dataRow()
	{
		// primary key
		// Long(1, pmroleid);

		// id data
		// String(2, predicate);
		// String(3, role);
		// String(4, pos);
		return null;
	}
}
