package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.*;
import org.sqlbuilder.vn.collector.VnSyntaxXmlProcessor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

public class Syntax implements HasId, Insertable, Comparable<Syntax>
{
	public static final Comparator<Syntax> COMPARATOR = Comparator.comparing(Syntax::getSyntax);

	public static final SetCollector2<Syntax> COLLECTOR = new SetCollector2<>(COMPARATOR);

	private static final VnSyntaxXmlProcessor SYNTAX_XML_PROCESSOR = new VnSyntaxXmlProcessor();

	private final String syntax;

	// C O N S T R U C T O R

	public static Syntax make(final String syntax) throws IOException, SAXException, ParserConfigurationException
	{
		String syntax2 = syntax.replaceFirst("^<SYNTAX>", "").replaceFirst("</SYNTAX>$", "");
		try
		{
			syntax2 = SYNTAX_XML_PROCESSOR.process(syntax2);
		}
		catch (ParserConfigurationException | IOException | SAXException e)
		{
			System.err.println(syntax2);
			throw e;
		}
		var s = new Syntax(syntax2);
		COLLECTOR.add(s);
		return s;
	}

	private Syntax(final String syntax)
	{
		this.syntax = syntax;
	}

	// A C C E S S

	public String getSyntax()
	{
		return syntax;
	}

	@Override
	public Integer getIntId()
	{
		return COLLECTOR.apply(this);
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
