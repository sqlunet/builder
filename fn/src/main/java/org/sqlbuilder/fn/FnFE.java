package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FEType;

public class FnFE implements HasID, Insertable<FnFE>
{
	public static final Set<FnFE> SET = new HashSet<>();

	private static final FnFEXmlProcessor definitionProcessor = new FnFEXmlProcessor();

	private final long frameid;

	private final FEType fe;

	private final Integer coreset;

	public final String definition;

	public FnFE(final long frameid, final FEType fe, final Integer coreset) throws ParserConfigurationException, IOException, SAXException
	{
		this.frameid = frameid;
		this.fe = fe;
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

	public long getID()
	{
		return fe.getID();
	}

	@Override
	public String dataRow()
	{
		return String.format("%d,%d,'%s','%s','%s',%d,%s", //
				fe.getID(), //
				frameid, //
				Utils.escape(fe.getName()), //
				Utils.escape(fe.getAbbrev()), //
				Utils.escape(definition), //
				fe.getCoreType().intValue(), //
				coreset == null ? "NULL" : coreset);
		//String(8, this.fe.getFgColor());
		//String(9, this.fe.getBgColor());
		//String(10, this.fe.getCDate());
		//String(11, this.fe.getCBy());
	}

	@Override
	public String toString()
	{
		return String.format("[FE feid=%s name=%s frameid=%s]", fe.getID(), fe.getName(), frameid);
	}
}
