package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnPos extends FnName
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_poses.insert");

	private static final String TABLE = Resources.resources.getString("Fn_poses.table");

	public FnPos(final String pos)
	{
		super(pos);
	}

	@Override
	protected String getSql()
	{
		return FnPos.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnPos.TABLE;
	}
}
