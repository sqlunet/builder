package org.sqlbuilder.fn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;
import org.sqlbuilder.common.SQLUpdateException;
import org.w3c.dom.Document;

import edu.berkeley.icsi.framenet.CorpDocType.Document;

public class FnDocument implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_documents.insert");

	private static final String TABLE = Resources.resources.getString("Fn_documents.table");

	private final long corpusid;

	public final Document doc;

	public FnDocument(final long corpusid, final Document doc)
	{
		this.corpusid = corpusid;
		this.doc = doc;
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnDocument.SQL_INSERT))
		{
			statement.setLong(1, this.doc.getID());
			statement.setLong(2, this.corpusid);
			statement.setString(3, this.doc.getDescription());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fndocument", FnDocument.TABLE, FnDocument.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[DOC id=%s corpusid=%s description=%s]", this.doc.getID(), this.corpusid, this.doc.getDescription());
	}
}
