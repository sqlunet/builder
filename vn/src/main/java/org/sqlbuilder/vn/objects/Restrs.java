package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.vn.collector.VnRestrsXmlProcessor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

public class Restrs implements HasId, Insertable, Comparable<Restrs>
{
	private static final boolean LOG_ONLY = false;

	public static final Comparator<Restrs> COMPARATOR = Comparator.comparing(Restrs::getValue).thenComparing(Restrs::isSyntactic);

	public static final SetCollector<Restrs> COLLECTOR = new SetCollector<>(COMPARATOR);

	private static final VnRestrsXmlProcessor RESTRS_XML_PROCESSOR = new VnRestrsXmlProcessor();

	private final String value;

	final boolean isSyntactic;

	// C O N S T R U C T O R
	public static Restrs make(final String value, final boolean isSyntactic) throws IOException, SAXException, ParserConfigurationException
	{
		var r = new Restrs(value, isSyntactic);
		COLLECTOR.add(r);
		return r;
	}

	private Restrs(final String value, final boolean isSyntactic0) throws IOException, SAXException, ParserConfigurationException
	{
		this.isSyntactic = isSyntactic0;
		try
		{
			this.value = RESTRS_XML_PROCESSOR.process(value);
			if (this.value == null || this.value.isEmpty())
			{
				if (LOG_ONLY)
				{
					System.err.println("empty [" + value + "] ->" + this.value);
				}
				else
				{
					throw new RuntimeException("empty [" + value + "] ->" + this.value);
				}
			}
		}
		catch (ParserConfigurationException | IOException | SAXException e)
		{
			System.err.println(value);
			throw e;
		}
	}

	// A C C E S S

	public String getValue()
	{
		return value;
	}

	public boolean isSyntactic()
	{
		return isSyntactic;
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
		Restrs that = (Restrs) o;
		return isSyntactic == that.isSyntactic && value.equals(that.value);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(value, isSyntactic);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Restrs that)
	{
		final int cmp = this.value.compareTo(that.value);
		if (cmp != 0)
		{
			return cmp;
		}
		return Boolean.compare(isSyntactic, that.isSyntactic);
	}

	// S T R I N G

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(this.value);
		if (this.isSyntactic)
		{
			sb.append('*');
		}
		return sb.toString();
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// id
		// value
		// isSyntactic
		return String.format("'%s',%b", value, isSyntactic);
	}
}
