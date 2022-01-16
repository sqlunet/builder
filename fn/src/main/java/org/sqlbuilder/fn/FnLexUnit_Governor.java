package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnLexUnit_Governor extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_lexunits_governors.insert");

	private static final String TABLE = Resources.resources.getString("Fn_lexunits_governors.table");

	public FnLexUnit_Governor(final long luid, final long governorid)
	{
		super(luid, governorid);
	}

	@Override
	protected String getSql()
	{
		return FnLexUnit_Governor.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnLexUnit_Governor.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[LU-GOV luid=%s governorid=%s]", this.id1, this.id2);
	}
}
