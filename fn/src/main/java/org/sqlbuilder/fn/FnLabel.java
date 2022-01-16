package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.LabelType;

public class FnLabel implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_labels.insert");

	private static final String TABLE = Resources.resources.getString("Fn_labels.table");

	private static long allocator = 0;

	private final long labelid;

	public final LabelType label;

	public final long layerid;

	public FnLabel(final long layerid, final LabelType label)
	{
		this.labelid = ++FnLabel.allocator;
		this.layerid = layerid;
		this.label = label;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnLabel.SQL_INSERT))
		{
			// labelid,layerid,labeltype,labelitypeid,feid,start,end,fgcolor,bgcolor,cby
			final int feid = this.label.getFeID();
			final int from = this.label.getStart();
			final int to = this.label.getEnd();

			statement.setLong(1, getId());
			statement.setLong(2, this.layerid);
			statement.setString(3, this.label.getName());
			if (this.label.getItype() != null)
			{
				statement.setInt(4, this.label.getItype().intValue());
			}
			else
			{
				statement.setNull(4, Types.INTEGER);
			}
			if (feid != 0)
			{
				statement.setInt(5, feid);
			}
			else
			{
				statement.setNull(5, Types.INTEGER);
			}
			if (from == 0 && to == 0)
			{
				statement.setNull(6, from);
				statement.setNull(7, to);
			}
			else
			{
				statement.setInt(6, from);
				statement.setInt(7, to);
			}
			statement.setString(8, this.label.getBgColor());
			statement.setString(9, this.label.getFgColor());
			statement.setString(10, this.label.getCBy());

			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnlabel", FnLabel.TABLE, FnLabel.SQL_INSERT, sqle);
		}
	}

	public long getId()
	{
		return this.labelid;
	}

	public static void reset()
	{
		FnLabel.allocator = 0;
	}

	@Override
	public String toString()
	{
		return String.format("[LAB labelid=%s label=%s layerid=%s]", getId(), this.label.getName(), this.layerid);
	}
}
