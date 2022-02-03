package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

public class VnSyntax implements Insertable, Comparable<VnSyntax>
{
	protected static final Set<VnSyntax> SET = new HashSet<>();

	public static Map<VnSyntax, Integer> MAP;

	private static final VnSyntaxXmlProcessor SYNTAX_XML_PROCESSOR = new VnSyntaxXmlProcessor();

	private final String syntax;

	// C O N S T R U C T

	public VnSyntax(final String syntax0) throws IOException, SAXException, ParserConfigurationException
	{
		final String syntax1 = syntax0.replaceFirst("^<SYNTAX>", "").replaceFirst("</SYNTAX>$", "");
		try
		{
			this.syntax = SYNTAX_XML_PROCESSOR.process(syntax1);
		}
		catch (ParserConfigurationException | IOException | SAXException e)
		{
			System.err.println(syntax1);
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
		VnSyntax that = (VnSyntax) o;
		return syntax.equals(that.syntax);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(syntax);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnSyntax that)
	{
		return this.syntax.compareTo(that.syntax);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// id
		// syntax
		return String.format("'%s'", syntax);
	}
}
