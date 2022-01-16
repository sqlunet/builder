package org.sqlbuilder.fn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.xml.parsers.ParserConfigurationException;

import org.sqlbuilder.Insertable;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLUpdateException;
import org.xml.sax.SAXException;

import edu.berkeley.icsi.framenet.FEType;

public class FnFE implements Insertable
{
	private static final String SQL_INSERT = Resources.resources.getString("Fn_fes.insert");

	private static final String TABLE = Resources.resources.getString("Fn_fes.table");

	private static final FnFEXmlProcessor definitionProcessor = new FnFEXmlProcessor();

	private final long frameid;

	private final FEType fe;

	private final Integer coreset;

	public final String definition;

	public FnFE(final long frameid, final FEType fe, final Integer coreset) throws IOException, SAXException, ParserConfigurationException
	{
		this.fe = fe;
		this.frameid = frameid;
		this.coreset = coreset;
		try
		{
			this.definition = FnFE.definitionProcessor.process(this.fe.getDefinition());
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println(this.fe.getDefinition());
			throw e;
		}
	}

	@Override
	public int insert(final Connection connection) throws SQLUpdateException
	{
		try (PreparedStatement statement = connection.prepareStatement(FnFE.SQL_INSERT))
		{
			statement.setLong(1, this.fe.getID());
			statement.setLong(2, this.frameid);
			statement.setString(3, this.fe.getName());
			statement.setString(4, this.fe.getAbbrev());
			statement.setString(5, this.definition);
			statement.setInt(6, this.fe.getCoreType().intValue());
			if (this.coreset != null)
			{
				statement.setInt(7, this.coreset);
			}
			else
			{
				statement.setNull(7, Types.INTEGER);
			}
			statement.setString(8, this.fe.getFgColor());
			statement.setString(9, this.fe.getBgColor());
			statement.setString(10, this.fe.getCDate());
			statement.setString(11, this.fe.getCBy());

			statement.executeUpdate();
			return 1;
		}
		catch (SQLException sqle)
		{
			throw new SQLUpdateException("fnfe", FnFE.TABLE, FnFE.SQL_INSERT, sqle);
		}
	}

	@Override
	public String toString()
	{
		return String.format("[FE feid=%s frameid=%s name=%s]", this.fe.getID(), this.frameid, this.fe.getName());
	}
}
