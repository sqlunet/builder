package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.FEGroupRealizationType.Pattern;

public class FnGroupPattern implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_patterns.insert");

	private static final String TABLE = Resources.resources.getString("Fn_patterns.table");

	private static long allocator = 0;

	private final long patternid;

	public final Pattern pattern;

	public final long fegrid;

	public FnGroupPattern(final long fegrid, final Pattern pattern)
	{
		this.patternid = ++FnGroupPattern.allocator;
		this.fegrid = fegrid;
		this.pattern = pattern;
	}

	public long getId()
	{
		return this.patternid;
	}

	public static void reset()
	{
		FnGroupPattern.allocator = 0;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnGroupPattern.SQL_INSERT))
		{
			statement.setLong(1, getId());
			statement.setLong(2, this.fegrid);
			statement.setLong(3, this.pattern.getTotal());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fngrouppattern", FnGroupPattern.TABLE, FnGroupPattern.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[GRPPAT patternid=%s fegrid=%s]", getId(), this.fegrid);
	}
}
