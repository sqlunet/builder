package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.SQLUpdateException;

public abstract class FnMap implements Insertable
{
	public final long id1;

	public final long id2;

	public FnMap(final long id1, final long id2)
	{
		this.id1 = id1;
		this.id2 = id2;
	}

	protected abstract String getSql();

	protected abstract String getTable();

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		if (this.id1 == 0 || this.id2 == 0)
			return 0;
		try (PreparedStatement statement = connection.prepareStatement(getSql()))
		{
			statement.setLong(1, this.id1);
			statement.setLong(2, this.id2);
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnmap", getTable(), getSql(), sqle);
		}
	}
}
