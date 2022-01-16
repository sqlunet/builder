package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnFrame_CoreFEMember extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_frames_corefes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_frames_corefes.table");

	public FnFrame_CoreFEMember(final long frameid, final long feid)
	{
		super(frameid, feid);
	}

	@Override
	protected String getSql()
	{
		return FnFrame_CoreFEMember.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnFrame_CoreFEMember.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[FR-coreFE frameid=%s feid=%s]", this.id1, this.id2);
	}
}
