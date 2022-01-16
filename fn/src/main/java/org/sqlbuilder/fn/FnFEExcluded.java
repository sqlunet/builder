package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnFEExcluded extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_fes_excluded.insert");

	private static final String TABLE = Resources.resources.getString("Fn_fes_excluded.table");

	public FnFEExcluded(final long feid, final long fe2id)
	{
		super(feid, fe2id);
	}

	@Override
	protected String getSql()
	{
		return FnFEExcluded.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnFEExcluded.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[FEexcl feid=%s fe2id=%s]", this.id1, this.id2);
	}
}
