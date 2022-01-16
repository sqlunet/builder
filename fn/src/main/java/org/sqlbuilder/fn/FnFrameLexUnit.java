package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.FrameLUType;

public class FnFrameLexUnit extends FnLexUnitBase implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_lexunits.insert2");

	private static final String TABLE = Resources.resources.getString("Fn_lexunits.table");

	public final long frameid;

	public final FrameLUType lu;

	public FnFrameLexUnit(final long frameid, final FrameLUType lu)
	{
		this.frameid = frameid;
		this.lu = lu;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		final Definition definition = FnLexUnitBase.getDefinition(this.lu.getDefinition());

		try (PreparedStatement statement = connection.prepareStatement(FnFrameLexUnit.SQL_INSERT))
		{
			statement.setLong(1, this.lu.getID());
			statement.setLong(2, this.frameid);
			statement.setString(3, this.lu.getName());
			statement.setInt(4, this.lu.getPOS().intValue());
			if (definition.def != null)
			{
				statement.setString(5, definition.def);
			}
			else
			{
				statement.setNull(5, java.sql.Types.CHAR);
			}
			if (definition.dict != null)
			{
				statement.setString(6, String.valueOf(definition.dict));
			}
			else
			{
				statement.setNull(6, java.sql.Types.CHAR);
			}
			statement.setString(7, this.lu.getStatus());
			statement.setString(8, this.lu.getIncorporatedFE());
			statement.setLong(9, this.lu.getSentenceCount().getAnnotated());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnframelexunit", FnFrameLexUnit.TABLE, FnFrameLexUnit.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[FRLU luid=%s name=%s frameid=%s]", this.lu.getID(), this.lu.getName(), this.frameid);
	}
}
