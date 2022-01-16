package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.SubCorpusType;

public class FnSubCorpus implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_subcorpuses.insert");

	private static final String TABLE = Resources.resources.getString("Fn_subcorpuses.table");

	private static long allocator = 0;

	private final long subcorpusid;

	public final SubCorpusType subcorpus;

	public final long luid;

	public FnSubCorpus(final long luid, final SubCorpusType subcorpus)
	{
		this.subcorpusid = ++FnSubCorpus.allocator;
		this.luid = luid;
		this.subcorpus = subcorpus;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnSubCorpus.SQL_INSERT))
		{
			statement.setLong(1, getId());
			statement.setLong(2, this.luid);
			statement.setString(3, this.subcorpus.getName());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnsubcorpus", FnSubCorpus.TABLE, FnSubCorpus.SQL_INSERT, sqle);
		}
	}

	public long getId()
	{
		return this.subcorpusid;
	}

	public static void reset()
	{
		FnSubCorpus.allocator = 0;
	}

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS id=%s name=%s luid=%s]", getId(), this.subcorpus.getName(), this.luid);
	}
}
