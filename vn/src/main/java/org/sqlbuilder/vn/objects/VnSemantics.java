package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.vn.collector.VnSemanticsXmlProcessor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

public class VnSemantics implements Insertable, Comparable<VnSemantics>
{
	public static final Comparator<VnSemantics> COMPARATOR = Comparator.comparing(VnSemantics::getSemantics);

	public static final SetCollector<VnSemantics> COLLECTOR = new SetCollector<>(COMPARATOR);

	private static final VnSemanticsXmlProcessor SEMANTICS_PROCESSOR = new VnSemanticsXmlProcessor();

	private final String semantics;

	// C O N S T R U C T

	public static VnSemantics make(final String semantics) throws IOException, SAXException, ParserConfigurationException
	{
		var s = new VnSemantics(semantics);
		COLLECTOR.add(s);
		return s;
	}

	private VnSemantics(final String semantics) throws IOException, SAXException, ParserConfigurationException
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
		VnSemantics that = (VnSemantics) o;
		return semantics.equals(that.semantics);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(semantics);
	}


	// O R D E R I N G

	@Override
	public int compareTo(final VnSemantics that)
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
