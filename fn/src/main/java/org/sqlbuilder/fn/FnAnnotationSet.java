package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;
import org.sqlbuilder.common.SQLUpdateException;

import edu.berkeley.icsi.framenet.AnnotationSetType;

public class FnAnnotationSet implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_annosets.insert");

	private static final String TABLE = Resources.resources.getString("Fn_annosets.table");

	public final AnnotationSetType annoset;

	public final long sentenceid;

	public final long frameid;

	public final long luid;

	private final boolean fromFullText;

	public FnAnnotationSet(final long sentenceid, final AnnotationSetType annoset)
	{
		super();
		this.annoset = annoset;
		this.sentenceid = sentenceid;
		this.frameid = 0;
		this.luid = 0;
		this.fromFullText = true;
	}

	public FnAnnotationSet(final long sentenceid, final AnnotationSetType annoset, final long frameid, final long luid)
	{
		super();
		this.annoset = annoset;
		this.sentenceid = sentenceid;
		this.frameid = frameid;
		this.luid = luid;
		this.fromFullText = false;
	}

	public long getId()
	{
		return this.annoset.getID() + (this.fromFullText ? 100000000L : 0L);
	}

	public long getFrameId()
	{
		return this.fromFullText ? this.annoset.getFrameID() : this.frameid;
	}

	public long getLuId()
	{
		return this.fromFullText ? this.annoset.getLuID() : this.luid;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnAnnotationSet.SQL_INSERT))
		{
			final long frameid2 = getFrameId();
			final long luid2 = getLuId();
			final long cxnid = this.annoset.getCxnID();

			statement.setLong(1, getId());
			statement.setLong(2, this.sentenceid);
			if (frameid2 != 0)
			{
				statement.setLong(3, frameid2);
			}
			else
			{
				statement.setNull(3, Types.INTEGER);
			}
			if (luid2 != 0)
			{
				statement.setLong(4, luid2);
			}
			else
			{
				statement.setNull(4, Types.INTEGER);
			}
			if (cxnid != 0)
			{
				statement.setLong(5, cxnid);
			}
			else
			{
				statement.setNull(5, Types.INTEGER);
			}
			statement.setString(6, this.annoset.getCxnName());
			statement.setString(7, this.annoset.getStatus());
			statement.setString(8, this.annoset.getCDate());
			return statement.executeUpdate();
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnannotationset", FnAnnotationSet.TABLE, FnAnnotationSet.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[AS annosetid=%s frameid=%s luid=%s]", getId(), getFrameId(), getLuId());
	}
}
