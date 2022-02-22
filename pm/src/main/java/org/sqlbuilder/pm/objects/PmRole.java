package org.sqlbuilder.pm.objects;

import org.sqlbuilder.common.Insertable;

public class PmRole implements Insertable
{
	public final String predicate;

	public final String role;

	public final Character pos;

	public PmRole(final String predicate, final String role, final Character pos)
	{
		// predicate, role, pos
		this.predicate = predicate;
		this.role = role;
		this.pos = pos;
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s','%s','%c'", predicate, role, pos);
	}

	@Override
	public String toString()
	{
		return String.format("predicate=%s role=%s pos=%s", predicate, role, pos);
	}
}
