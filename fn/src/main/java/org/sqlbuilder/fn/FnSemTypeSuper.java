package org.sqlbuilder.fn;

import org.sqlbuilder.Resources;

public class FnSemTypeSuper extends FnMap
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_semtypes_supers.insert");

	private static final String TABLE = Resources.resources.getString("Fn_semtypes_supers.table");

	public FnSemTypeSuper(final long semtypeid, final long supersemtypeid)
	{
		super(semtypeid, supersemtypeid);
	}

	@Override
	protected String getSql()
	{
		return FnSemTypeSuper.SQL_INSERT;
	}

	@Override
	protected String getTable()
	{
		return FnSemTypeSuper.TABLE;
	}

	@Override
	public String toString()
	{
		return String.format("[SEMsuper semtypeid=%s supersemtypeid=%s]", this.id1, this.id2);
	}
}
