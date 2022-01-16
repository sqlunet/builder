package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnFE_SemType extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_fes_semtypes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_fes_semtypes.table");

	public FnFE_SemType(final long feid, final long semtypeid)
	{
		super(feid, semtypeid);
	}

	@Override
	protected String getSql()
	{
		return FnFE_SemType.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnFE_SemType.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[FE-SEM feid=%s semtypeid=%s]", this.id1, this.id2);
	}
}
