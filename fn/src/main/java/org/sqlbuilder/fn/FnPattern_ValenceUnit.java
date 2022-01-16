package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

public class FnPattern_ValenceUnit implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_patterns_valenceunits.insert");

	private static final String TABLE = Resources.resources.getString("Fn_patterns_valenceunits.table");

	public final long patternid;

	public final long vuid;

	public final String fe;

	public FnPattern_ValenceUnit(final long patternid, final long vuid, final String fe)
	{
		this.patternid = patternid;
		this.vuid = vuid;
		this.fe = fe;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnPattern_ValenceUnit.SQL_INSERT))
		{
			statement.setLong(1, this.patternid);
			statement.setLong(2, this.vuid);
			statement.setString(3, this.fe);
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnpattern_vu", FnPattern_ValenceUnit.TABLE, FnPattern_ValenceUnit.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[PAT-VU patternid=%s vuid=%s fe=%s]", this.patternid, this.vuid, this.fe);
	}
}
