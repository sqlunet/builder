package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnFERequired extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_fes_required.insert");

	private static final String TABLE = Resources.resources.getString("Fn_fes_required.table");

	public FnFERequired(final long feid, final long fe2id)
	{
		super(feid, fe2id);
	}

	@Override
	protected String getSql()
	{
		return FnFERequired.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnFERequired.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[FEreq feid=%s fe2id=%s]", this.id1, this.id2);
	}
}
