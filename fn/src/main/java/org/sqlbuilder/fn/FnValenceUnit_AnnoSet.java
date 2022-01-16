package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnValenceUnit_AnnoSet extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_valenceunits_annosets.insert");

	private static final String TABLE = Resources.resources.getString("Fn_valenceunits_annosets.table");

	public FnValenceUnit_AnnoSet(final long vuid, final long annosetid)
	{
		super(vuid, annosetid);
	}

	@Override
	protected String getSql()
	{
		return FnValenceUnit_AnnoSet.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnValenceUnit_AnnoSet.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[VU-AS vuid=%s annosetid=%s]", this.id1, this.id2);
	}
}
