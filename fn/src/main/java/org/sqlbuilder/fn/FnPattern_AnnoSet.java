package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnPattern_AnnoSet extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_patterns_annosets.insert");

	private static final String TABLE = Resources.resources.getString("Fn_patterns_annosets.table");

	public FnPattern_AnnoSet(final long patternid, final long annosetid)
	{
		super(patternid, annosetid);
	}

	@Override
	protected String getSql()
	{
		return FnPattern_AnnoSet.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnPattern_AnnoSet.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[PAT-AS patternid=%s annosetid=%s]", this.id1, this.id2);
	}
}
