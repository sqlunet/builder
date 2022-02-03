package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

public class Frame implements Insertable, Comparable<Frame>
{
	public static final Comparator<Frame> COMPARATOR = Comparator //
			.comparing(Frame::getName) //
			.thenComparing(Frame::getSubName, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(Frame::getDescriptionNumber) //
			.thenComparing(Frame::getDescriptionXTag) //
			.thenComparing(Frame::getSyntax) //
			.thenComparing(Frame::getSemantics);

	public static final SetCollector<Frame> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String descriptionNumber;

	private final String descriptionXTag;

	private final FrameName name;

	private final FrameSubName subName;

	private final Syntax syntax;

	private final Semantics semantics;

	// C O N S T R U C T

	public static Frame make(final String descriptionNumber, final String descriptionXTag, final String descriptionPrimary, final String descriptionSecondary, final String syntax, final String semantics) throws ParserConfigurationException, SAXException, IOException
	{
		var f = new Frame(descriptionNumber, descriptionXTag, descriptionPrimary, descriptionSecondary, syntax, semantics);
		COLLECTOR.add(f);
		return f;
	}

	private Frame(final String descriptionNumber, final String descriptionXTag, final String descriptionPrimary, final String descriptionSecondary, final String syntax, final String semantics) throws ParserConfigurationException, SAXException, IOException
	{
		this.descriptionNumber = descriptionNumber;
		this.descriptionXTag = descriptionXTag;
		this.name = FrameName.make(descriptionPrimary);
		this.subName = descriptionSecondary == null || descriptionSecondary.isEmpty() ? null : FrameSubName.make(descriptionSecondary);
		this.syntax = Syntax.make(syntax);
		this.semantics = Semantics.make(semantics);
	}

	// A C C E S S

	public FrameName getName()
	{
		return name;
	}

	public FrameSubName getSubName()
	{
		return subName;
	}

	public String getDescriptionNumber()
	{
		return descriptionNumber;
	}

	public String getDescriptionXTag()
	{
		return descriptionXTag;
	}

	public Syntax getSyntax()
	{
		return syntax;
	}

	public Semantics getSemantics()
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
		Frame that = (Frame) o;
		return descriptionNumber.equals(that.descriptionNumber) && descriptionXTag.equals(that.descriptionXTag) && name.equals(that.name) && Objects.equals(subName, that.subName) && Objects.equals(syntax, that.syntax) && Objects.equals(semantics, that.semantics);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(descriptionNumber, descriptionXTag, name, subName, syntax, semantics);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Frame that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return " number=" + this.descriptionNumber + " tag=" + this.descriptionXTag + " description1=" + this.name + " description2=" + this.subName + " syntax=" + this.syntax + " semantics=" + this.semantics;
	}

	// I N S E R T

	@RequiresIdFrom(type = FrameName.class)
	@RequiresIdFrom(type = FrameSubName.class)
	@RequiresIdFrom(type = Syntax.class)
	@RequiresIdFrom(type = Semantics.class)
	@Override
	public String dataRow()
	{
		// id
		// descriptionNumber (or NULL)
		// descriptionXTag
		// name.id
		// subName.id
		// syntax.id
		// semantics.id
		return String.format("%s,'%s',%d,%d,%d,%d", //
				descriptionNumber != null ? "'" + descriptionNumber + "'" : "NULL", //
				descriptionXTag, //
				FrameName.COLLECTOR.get(name), //
				FrameSubName.COLLECTOR.get(subName), //
				Syntax.COLLECTOR.get(syntax), //
				Semantics.COLLECTOR.get(semantics));
	}
}
