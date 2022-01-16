package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.FEValenceType;

public class FnFEGroupRealization_Fe implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_fegrouprealizations_fes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_fegrouprealizations_fes.table");

	private static long allocator = 0;

	private final long rfeid;

	public final FEValenceType fe;

	public final long fegrid;

	public FnFEGroupRealization_Fe(final long fegrid, final FEValenceType fe)
	{
		this.rfeid = ++FnFEGroupRealization_Fe.allocator;
		this.fegrid = fegrid;
		this.fe = fe;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnFEGroupRealization_Fe.SQL_INSERT))
		{
			statement.setLong(1, this.rfeid);
			statement.setLong(2, this.fegrid);
			statement.setString(3, this.fe.getName());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnfegrouprealization_fe", FnFEGroupRealization_Fe.TABLE, FnFEGroupRealization_Fe.SQL_INSERT, sqle);
		}
	}

	public long getId()
	{
		return this.rfeid;
	}

	public static void reset()
	{
		FnFEGroupRealization_Fe.allocator = 0;
	}

	@Override
	public String toString()
	{
		return String.format("[FEGR-FE ferid=%s fe=%s]", this.fegrid, this.fe.getName());
	}
}
