package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.CorpDocType;

public class FnCorpus implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_corpuses.insert");

	private static final String TABLE = Resources.resources.getString("Fn_corpuses.table");

	public final CorpDocType corpus;

	public final long luid;

	public FnCorpus(final CorpDocType corpus, final long luid)
	{
		this.corpus = corpus;
		this.luid = luid;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnCorpus.SQL_INSERT))
		{
			statement.setLong(1, this.corpus.getID());
			statement.setString(2, this.corpus.getName());
			statement.setString(3, this.corpus.getDescription());
			if (this.luid != 0)
			{
				statement.setLong(4, this.luid);
			}
			else
			{
				statement.setNull(4, Types.INTEGER);
			}
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fncorpus", FnCorpus.TABLE, FnCorpus.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[CORPUS corpusid=%s name=%s]", this.corpus.getID(), this.corpus.getName());
	}
}
