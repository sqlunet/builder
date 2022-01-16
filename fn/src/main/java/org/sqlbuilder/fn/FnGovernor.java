package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.GovernorType;

public class FnGovernor implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_governors.insert");

	private static final String TABLE = Resources.resources.getString("Fn_governors.table");

	private static long allocator = 0;

	private final long governorid;

	public final long fnwordid;

	public final GovernorType governor;

	public FnGovernor(final long wordid, final GovernorType governor)
	{
		this.governorid = ++FnGovernor.allocator;
		this.fnwordid = wordid;
		this.governor = governor;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnGovernor.SQL_INSERT))
		{
			statement.setLong(1, getId());
			// statement.setString(2, this.governor.getLemma());
			statement.setLong(2, this.fnwordid);
			statement.setString(3, this.governor.getType());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fngovernor", FnGovernor.TABLE, FnGovernor.SQL_INSERT, sqle);
		}
	}

	public long getId()
	{
		return this.governorid;
	}

	public static void reset()
	{
		FnGovernor.allocator = 0;
	}

	@Override
	public String toString()
	{
		return String.format("[GOV lemma=%s type=%s]", this.governor.getLemma(), this.governor.getType());
	}
}
