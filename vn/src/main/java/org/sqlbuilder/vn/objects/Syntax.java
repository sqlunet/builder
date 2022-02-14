package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.vn.collector.VnSyntaxXmlProcessor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

public class Syntax implements HasId, Insertable, Comparable<Syntax>
{
	public static final Comparator<Syntax> COMPARATOR = Comparator.comparing(Syntax::getSyntax);

	public static final SetCollector<Syntax> COLLECTOR = new SetCollector<>(COMPARATOR);

	private static final VnSyntaxXmlProcessor SYNTAX_XML_PROCESSOR = new VnSyntaxXmlProcessor();

	private final String syntax;

	// C O N S T R U C T O R
	public static Syntax make(final String syntax) throws IOException, SAXException, ParserConfigurationException
	{
		var s = new Syntax(syntax);
		COLLECTOR.add(s);
		return s;
	}

	private Syntax(final String syntax) throws IOException, SAXException, ParserConfigurationException
	{
		final String syntax1 = syntax.replaceFirst("^<SYNTAX>", "").replaceFirst("</SYNTAX>$", "");
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

	// A C C E S S

	public String getSyntax()
	{
		return syntax;
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
		Syntax that = (Syntax) o;
		return syntax.equals(that.syntax);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(syntax);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Syntax that)
	{
		return this.syntax.compareTo(that.syntax);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// id
		// syntax
		return String.format("'%s'", Utils.escape(syntax));
	}
}
