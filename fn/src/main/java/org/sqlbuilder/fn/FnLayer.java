package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.LayerType;

public class FnLayer implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_layers.insert");

	private static final String TABLE = Resources.resources.getString("Fn_layers.table");

	private static long allocator = 0;

	private final long layerid;

	public final LayerType layer;

	public final long annosetid;

	public FnLayer(final long annosetid, final LayerType layer)
	{
		this.layerid = ++FnLayer.allocator;
		this.annosetid = annosetid;
		this.layer = layer;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnLayer.SQL_INSERT))
		{
			statement.setLong(1, getId());
			statement.setLong(2, this.annosetid);
			statement.setString(3, this.layer.getName());
			statement.setInt(4, this.layer.getRank());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnlayer", FnLayer.TABLE, FnLayer.SQL_INSERT, sqle);
		}
	}

	public long getId()
	{
		return this.layerid;
	}

	public static void reset()
	{
		FnLayer.allocator = 0;
	}

	@Override
	public String toString()
	{
		return String.format("[LAY layerid=%s layer=%s annosetid=%s]", getId(), this.layer.getName(), this.annosetid);
	}
}
