package org.sqlbuilder.fn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;
import org.xml.sax.SAXException;

import edu.berkeley.icsi.framenet.FrameDocument.Frame;

public class FnFrame implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_frames.insert");

	private static final String TABLE = Resources.resources.getString("Fn_frames.table");

	private static final FnFrameXmlProcessor definitionProcessor = new FnFrameXmlProcessor();

	public final Frame frame;

	public final String definition;

	public FnFrame(final Frame frame) throws IOException, SAXException, ParserConfigurationException
	{
		super();
		this.frame = frame;
		try
		{
			this.definition = FnFrame.definitionProcessor.process(this.frame.getDefinition());
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println(this.frame.getDefinition());
			throw e;
		}
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnFrame.SQL_INSERT))
		{
			statement.setLong(1, this.frame.getID());
			statement.setString(2, this.frame.getName());
			statement.setString(3, this.definition);
			statement.setString(4, this.frame.getCDate());
			statement.setString(5, this.frame.getCBy());
			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnframe", FnFrame.TABLE, FnFrame.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[FRAME frid=%s name=%s]", this.frame.getID(), this.frame.getName());
	}
}
