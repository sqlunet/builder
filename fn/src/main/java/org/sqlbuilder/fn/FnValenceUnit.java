package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.ValenceUnitType;

public class FnValenceUnit extends FnValenceUnitBase implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_valenceunits.insert");

	private static final String TABLE = Resources.resources.getString("Fn_valenceunits.table");

	// A L L O C A T O R

	private static long allocator = 0;

	public static void reset()
	{
		FnValenceUnit.allocator = 0;
	}

	// M E M B E R S

	private final long vuid;

	public final long ferid;

	// C O N S T R U C T O R

	public FnValenceUnit(final long ferid, final ValenceUnitType vu)
	{
		super(vu);
		this.vuid = ++FnValenceUnit.allocator;
		this.ferid = ferid;
		FnValenceUnitBase.map.put(this, getId());
	}

	// I D E N T I T Y

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return super.equals(obj);
	}

	// A C C E S S

	public long getId()
	{
		return this.vuid;
	}

	// I N S E R T

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		String pt = this.vu.getPT();
		if (pt.isEmpty())
		{
			pt = null;
		}
		String gf = this.vu.getGF();
		if (gf.isEmpty())
		{
			gf = null;
		}

		try (PreparedStatement statement = connection.prepareStatement(FnValenceUnit.SQL_INSERT))
		{
			statement.setLong(1, getId());
			statement.setLong(2, this.ferid);
			if (pt != null)
			{
				statement.setString(3, pt);
			}
			else
			{
				statement.setNull(3, Types.VARCHAR);
			}
			if (gf != null)
			{
				statement.setString(4, gf);
			}
			else
			{
				statement.setNull(4, Types.VARCHAR);
			}
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnvalenceunit", FnValenceUnit.TABLE, FnValenceUnit.SQL_INSERT, sqle);
		}
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[VU feid=%s fe=%s pt=%s gf=%s]", this.ferid, this.vu.getFE(), this.vu.getPT(), this.vu.getGF());
	}
}
