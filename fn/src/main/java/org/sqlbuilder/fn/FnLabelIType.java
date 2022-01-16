package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnLabelIType extends FnName
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_labelitypes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_labelitypes.table");

	public FnLabelIType(final String labelitype)
	{
		super(labelitype);
	}

	@Override
	protected String getSql()
	{
		return FnLabelIType.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnLabelIType.TABLE;
	}
}
