package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnGovernor_AnnoSet extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_governors_annosets.insert");

	private static final String TABLE = Resources.resources.getString("Fn_governors_annosets.table");

	public FnGovernor_AnnoSet(final long governorid, final long annosetid)
	{
		super(governorid, annosetid);
	}

	@Override
	protected String getSql()
	{
		return FnGovernor_AnnoSet.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnGovernor_AnnoSet.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[GOV-AS governorid=%s annosetid=%s]", this.id1, this.id2);
	}
}
