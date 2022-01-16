package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.SemTypeType;

public class FnSemType implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_semtypes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_semtypes.table");

	public final SemTypeType semtype;

	public FnSemType(final SemTypeType semtype)
	{
		super();
		this.semtype = semtype;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnSemType.SQL_INSERT))
		{
			statement.setLong(1, this.semtype.getID());
			statement.setString(2, this.semtype.getName());
			statement.setString(3, this.semtype.getAbbrev());
			statement.setString(4, this.semtype.getDefinition());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnsemtype", FnSemType.TABLE, FnSemType.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[SEM semtypeid=%s name=%s]", this.semtype.getID(), this.semtype.getName());
	}
}
