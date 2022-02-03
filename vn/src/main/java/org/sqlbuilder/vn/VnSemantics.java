package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

public class VnSemantics implements Insertable, Comparable<VnSemantics>
{
	protected static final Set<VnSemantics> SET = new HashSet<>();

	public static Map<VnSemantics, Integer> MAP;

	private static final VnSemanticsXmlProcessor SEMANTICS_PROCESSOR = new VnSemanticsXmlProcessor();

	private final String semantics;

	// C O N S T R U C T

	public VnSemantics(final String semantics0) throws IOException, SAXException, ParserConfigurationException
	{
		final String semantics2 = semantics0.replaceFirst("^<SEMANTICS>", "").replaceFirst("</SEMANTICS>$", "");
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
