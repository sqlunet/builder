package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.common.HasID;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.fn.collectors.FnFEXmlProcessor;
import org.sqlbuilder.fn.joins.Pair;
import org.sqlbuilder.fn.types.FeType;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FEType;

public class FE implements HasID, Insertable
{
	public static final Set<FE> SET = new HashSet<>();

	public static Map<Pair<Integer, Integer>, FE> BY_FETYPEID_AND_FRAMEID;

	private static final FnFEXmlProcessor definitionProcessor = new FnFEXmlProcessor();

	private final int feid;

	public final String name;

	public final String abbrev;

	private final int coretype;

	private final Integer coreset;

	public final String definition;

	private final int frameid;

	@SuppressWarnings("UnusedReturnValue")
	public static FE make(final FEType fe, final Integer coreset, final int frameid) throws ParserConfigurationException, IOException, SAXException
	{
		var e = new FE(fe, coreset, frameid);
		SET.add(e);
		return e;
	}

	private FE(final FEType fe, final Integer coreset, final int frameid) throws ParserConfigurationException, IOException, SAXException
	{
		this.feid = fe.getID();
		this.name = fe.getName();
		this.abbrev = fe.getAbbrev();
		this.coretype = fe.getCoreType().intValue();
		this.coreset = coreset;
		this.frameid = frameid;
		try
		{
			this.definition = FE.definitionProcessor.process(fe.getDefinition());
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println(fe.getDefinition());
			throw e;
		}
		FeType.COLLECTOR.add(fe.getName());
	}

	// A C C E S S

	public int getID()
	{
		return feid;
	}

	public String getName()
	{
		return name;
	}

	public String getAbbrev()
	{
		return abbrev;
	}

	public int getFrameID()
	{
		return frameid;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		FE that = (FE) o;
		return feid == that.feid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(feid);
	}

	// O R D E R

	public static final Comparator<FE> COMPARATOR = Comparator.comparing(FE::getName).thenComparing(FE::getID);

	// I N S E R T

	@RequiresIdFrom(type = FeType.class)
	@Override
	public String dataRow()
	{
		// (feid,fetypeid,feabbrev,fedefinition,coretypeid,coreset,frameid)
		return String.format("%d,%s,'%s','%s',%d,%s,%d", //
				feid, //
				FeType.getSqlId(name), //
				Utils.escape(abbrev), //
				Utils.escape(definition), //
				coretype, //
				Utils.nullableInt(coreset), //
				frameid); //
	}

	@Override
	public String comment()
	{
		return String.format("type=%s", name);
	}

	@Override
	public String toString()
	{
		return String.format("[FE feid=%s name=%s frameid=%s]", feid, name, frameid);
	}
}
