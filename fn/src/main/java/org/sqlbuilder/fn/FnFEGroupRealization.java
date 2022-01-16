package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.FEGroupRealizationType;

public class FnFEGroupRealization implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_fegrouprealizations.insert");

	private static final String TABLE = Resources.resources.getString("Fn_fegrouprealizations.table");

	private static long allocator = 0;

	private final long fegrid;

	public final FEGroupRealizationType fer;

	public final long luid;

	public FnFEGroupRealization(final long luid, final FEGroupRealizationType fer)
	{
		this.fegrid = ++FnFEGroupRealization.allocator;
		this.luid = luid;
		this.fer = fer;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		// fegrid INTEGER NOT NULL,
		// luid INTEGER,
		// total INTEGER,

		try (PreparedStatement statement = connection.prepareStatement(FnFEGroupRealization.SQL_INSERT))
		{
			statement.setLong(1, getId());
			statement.setLong(2, this.luid);
			statement.setLong(3, this.fer.getTotal());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnfegrouprealization", FnFEGroupRealization.TABLE, FnFEGroupRealization.SQL_INSERT, sqle);
		}
	}

	public long getId()
	{
		return this.fegrid;
	}

	public static void reset()
	{
		FnFEGroupRealization.allocator = 0;
	}

	@Override
	public String toString()
	{
		return String.format("[FEGR ferid=%s luid=%s]", getId(), this.luid);
	}
}
