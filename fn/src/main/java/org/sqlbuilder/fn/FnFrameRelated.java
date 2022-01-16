package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

public class FnFrameRelated implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_frames_related.insert");

	private static final String TABLE = Resources.resources.getString("Fn_frames_related.table");

	private final long frameid;

	private final String frame2;

	private final String relation;

	public FnFrameRelated(final long frameid, final String frame2, final int frame2id, final String relation)
	{
		this.frameid = frameid;
		this.frame2 = frame2;
		this.relation = relation;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnFrameRelated.SQL_INSERT))
		{
			statement.setLong(1, this.frameid);
			statement.setString(2, this.frame2);
			statement.setString(3, this.relation);
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnframerelated", FnFrameRelated.TABLE, FnFrameRelated.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[FRrel frameid=%s frame2=%s type=%s]", this.frameid, this.frame2, this.relation);
	}
}
