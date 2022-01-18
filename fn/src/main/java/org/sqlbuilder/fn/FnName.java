package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

public abstract class FnName implements Insertable<FnName>
{
	public final String name;

	public FnName(final String name)
	{
		this.name = name;
	}

	@Override
	public String dataRow()
	{
		// String(1, this.name);
		return null;
	}
}
