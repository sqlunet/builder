package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.FERealizationType;

public class FnFERealization implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_ferealizations.insert");

	private static final String TABLE = Resources.resources.getString("Fn_ferealizations.insert");

	private static long allocator = 0;

	private final long ferid;

	public final FERealizationType fer;

	public final long luid;

	public FnFERealization(final long luid, final FERealizationType fer)
	{
		this.ferid = ++FnFERealization.allocator;
		this.luid = luid;
		this.fer = fer;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnFERealization.SQL_INSERT))
		{
			statement.setLong(1, getId());
			statement.setLong(2, this.luid);
			statement.setString(3, this.fer.getFE().getName());
			statement.setLong(4, this.fer.getTotal());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnferealization", FnFERealization.TABLE, FnFERealization.SQL_INSERT, sqle);
		}
	}

	public long getId()
	{
		return this.ferid;
	}

	public static void reset()
	{
		FnFERealization.allocator = 0;
	}

	@Override
	public String toString()
	{
		return String.format("[FER ferid=%s luid=%s fe=%s]", getId(), this.luid, this.fer.getFE());
	}
}
