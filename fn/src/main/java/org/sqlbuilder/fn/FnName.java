package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.SQLUpdateException;

public abstract class FnName implements Insertable
{
	public final String name;

	public FnName(final String name)
	{
		this.name = name;
	}

	protected abstract String getSql();

	protected abstract String getTable();

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(getSql()))
		{
			statement.setString(1, this.name);
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnname", getTable(), getSql(), sqle);
		}
	}
}
