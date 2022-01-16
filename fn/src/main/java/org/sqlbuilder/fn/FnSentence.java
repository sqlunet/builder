package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.SentenceType;

public class FnSentence implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_sentences.insert");

	private static final String TABLE = Resources.resources.getString("Fn_sentences.table");

	public final SentenceType sentence;

	private final boolean fromFullText;

	public FnSentence(final SentenceType sentence, final boolean fromFullText)
	{
		super();
		this.sentence = sentence;
		this.fromFullText = fromFullText;
	}

	public long getId()
	{
		return this.sentence.getID() + (this.fromFullText ? 100000000L : 0L);
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnSentence.SQL_INSERT))
		{
			final int corpusid = this.sentence.getCorpID();
			final int documentid = this.sentence.getDocID();
			final long sentenceid = getId();

			statement.setLong(1, sentenceid);
			if (corpusid != 0)
			{
				statement.setInt(2, corpusid);
			}
			else
			{
				statement.setNull(2, Types.INTEGER);
			}
			if (documentid != 0)
			{
				statement.setInt(3, documentid);
			}
			else
			{
				statement.setNull(3, Types.INTEGER);
			}
			statement.setInt(4, this.sentence.getParagNo());
			statement.setInt(5, this.sentence.getSentNo());
			statement.setString(6, this.sentence.getText());
			statement.setInt(7, this.sentence.getAPos());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnsentence", FnSentence.TABLE, FnSentence.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[SENT sentenceid=%s id=%s corpusid=%s docid=%s]", getId(), this.sentence.getID(), this.sentence.getCorpID(), this.sentence.getDocID());
	}
}
