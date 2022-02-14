package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.vn.collector.VnSemanticsXmlProcessor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

public class Semantics implements HasId, Insertable, Comparable<Semantics>
{
	public static final Comparator<Semantics> COMPARATOR = Comparator.comparing(Semantics::getSemantics);

	public static final SetCollector<Semantics> COLLECTOR = new SetCollector<>(COMPARATOR);

	private static final VnSemanticsXmlProcessor SEMANTICS_PROCESSOR = new VnSemanticsXmlProcessor();

	private final String semantics;

	// C O N S T R U C T O R
	public static Semantics make(final String semantics) throws IOException, SAXException, ParserConfigurationException
	{
		var s = new Semantics(semantics);
		COLLECTOR.add(s);
		return s;
	}

	private Semantics(final String semantics) throws IOException, SAXException, ParserConfigurationException
	{
		final String semantics2 = semantics.replaceFirst("^<SEMANTICS>", "").replaceFirst("</SEMANTICS>$", "");
		try
		{
			this.semantics = SEMANTICS_PROCESSOR.process(semantics2);
		}
		catch (ParserConfigurationException | IOException | SAXException e)
		{
			System.err.println(semantics2);
			throw e;
		}
	}

	// A C C E S S

	public String getSemantics()
	{
		return semantics;
	}

	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
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
		Semantics that = (Semantics) o;
		return semantics.equals(that.semantics);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(semantics);
	}


	// O R D E R I N G

	@Override
	public int compareTo(final Semantics that)
	{
		return this.semantics.compareTo(that.semantics);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		//	id
		//	semantics
		return String.format("'%s'", semantics);
	}
}
