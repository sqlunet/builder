package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnCoreType extends FnName
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_coretypes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_coretypes.table");

	public FnCoreType(final String coretype)
	{
		super(coretype);
	}

	@Override
	protected String getSql()
	{
		return FnCoreType.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnCoreType.TABLE;
	}
}
