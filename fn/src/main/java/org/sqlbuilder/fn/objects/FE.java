package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;
import org.sqlbuilder.fn.collectors.FnFEXmlProcessor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FEType;

public class FE implements HasID, Insertable<FE>
{
	public static final Set<FE> SET = new HashSet<>();

	private static final FnFEXmlProcessor definitionProcessor = new FnFEXmlProcessor();

	private final FEType fe;

	private final Integer coreset;

	public final String definition;

	private final int frameid;

	public FE(final FEType fe, final Integer coreset, final int frameid) throws ParserConfigurationException, IOException, SAXException
	{
		this.fe = fe;
		this.coreset = coreset;
		try
		{
			this.definition = FE.definitionProcessor.process(this.fe.getDefinition());
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println(this.fe.getDefinition());
			throw e;
		}
		this.frameid = frameid;
	}

	public long getID()
	{
		return fe.getID();
	}

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s','%s',%d,%s,%d", //
				fe.getID(), //
				Utils.escape(fe.getName()), //
				Utils.escape(fe.getAbbrev()), //
				Utils.escape(definition), //
				fe.getCoreType().intValue(), //
				coreset == null ? "NULL" : coreset, //
				frameid); //
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
