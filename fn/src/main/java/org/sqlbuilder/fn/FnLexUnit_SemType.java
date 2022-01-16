package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnLexUnit_SemType extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_lexunits_semtypes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_lexunits_semtypes.table");

	public FnLexUnit_SemType(final long luid, final long semtypeid)
	{
		super(luid, semtypeid);
	}

	@Override
	protected String getSql()
	{
		return FnLexUnit_SemType.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnLexUnit_SemType.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[LU-SEM luid=%s semtypeid=%s]", this.id1, this.id2);
	}
}
