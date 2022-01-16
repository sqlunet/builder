package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.NotFoundException;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;
import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.SQLUpdateException;
import org.sqlbuilder.wordnet.id.WordId;

public class FnWord implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_words.insert");

	private static final String SQL_SELECT = Resources.resources.getString("Fn_words.select");

	private static final String TABLE = Resources.resources.getString("Fn_words.table");

	private static long allocator = 0;

	private Long id;

	public final String word;

	public final WordId wordid;

	public FnWord(final String lemma, final WordId wordid)
	{
		this.id = null;
		this.wordid = wordid;
		this.word = lemma;
	}

	private long find(final Connection connection) throws SQLException, NotFoundException
	{
		return FnWord.find(this.word, connection);
	}

	public static long find(final String word, final Connection connection) throws SQLException, NotFoundException
	{

		try (PreparedStatement statement = connection.prepareStatement(FnWord.SQL_SELECT))
		{
			statement.setString(1, word);

			try (ResultSet resultSet = statement.executeQuery())
			{
				if (!resultSet.isBeforeFirst())
					throw new NotFoundException(word);

				// noinspection LoopStatementThatDoesntLoop
				while (resultSet.next())
					return resultSet.getLong(1);
				throw new IllegalStateException(word);
			}
		}
	}

	private static long allocate()
	{
		return ++FnWord.allocator;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		// check if already there
		try
		{
			this.id = find(connection);
			return 0;
		}
		catch (NotFoundException | SQLException e)
		{
			// fall through
		}

		// allocate new id
		this.id = FnWord.allocate();

		// insert
		try (PreparedStatement statement = connection.prepareStatement(FnWord.SQL_INSERT))
		{
			statement.setLong(1, getId());
			if (this.wordid != null)
			{
				statement.setLong(2, this.wordid.toLong());
			}
			else
			{
				statement.setNull(2, Types.INTEGER);
			}
			statement.setString(3, this.word);
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnword", FnWord.TABLE, FnWord.SQL_INSERT, sqle);
		}
	}

	public long getId()
	{
		// exception
		return this.id;
	}

	public static void reset()
	{
		FnWord.allocator = 0;
	}
}
