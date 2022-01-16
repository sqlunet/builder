package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.LexemeType;

public class FnLexeme implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_lexemes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_lexemes.table");

	private static long allocator = 0;

	private final long lexemeid;

	public final LexemeType lexeme;

	public final long luid;

	public final long fnwordid;

	public FnLexeme(final long luid, final long fnwordid, final LexemeType type)
	{
		this.lexemeid = ++FnLexeme.allocator;
		this.luid = luid;
		this.fnwordid = fnwordid;
		this.lexeme = type;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		final int idx = this.lexeme.getOrder();
		try (PreparedStatement statement = connection.prepareStatement(FnLexeme.SQL_INSERT))
		{
			statement.setLong(1, getId());
			statement.setLong(2, this.luid);
			// statement.setString(3, getWord());
			statement.setLong(3, this.fnwordid);
			statement.setInt(4, this.lexeme.getPOS().intValue());
			statement.setBoolean(5, this.lexeme.getBreakBefore());
			statement.setBoolean(6, this.lexeme.getHeadword());
			if (idx != 0)
			{
				statement.setInt(7, idx);
			}
			else
			{
				statement.setNull(7, Types.INTEGER);
			}
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnlexeme", FnLexeme.TABLE, FnLexeme.SQL_INSERT, sqle);
		}
	}

	public long getId()
	{
		return this.lexemeid;
	}

	public static void reset()
	{
		FnLexeme.allocator = 0;
	}

	public String getWord()
	{
		return FnLexeme.makeWord(this.lexeme.getName());
	}

	public static String makeWord(final String string)
	{
		return string.replaceAll("_*\\(.*$", "");
	}

	@Override
	public String toString()
	{
		return String.format("[LEX luid=%s name=%s]", this.luid, getWord());
	}
}
