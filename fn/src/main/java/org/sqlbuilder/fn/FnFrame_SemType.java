package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnFrame_SemType extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_frames_semtypes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_frames_semtypes.table");

	public FnFrame_SemType(final long frameid, final long semtypeid)
	{
		super(frameid, semtypeid);
	}

	@Override
	protected String getSql()
	{
		return FnFrame_SemType.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnFrame_SemType.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[FR-SEM frameid=%s semtypeid=%s]", this.id1, this.id2);
	}
}
