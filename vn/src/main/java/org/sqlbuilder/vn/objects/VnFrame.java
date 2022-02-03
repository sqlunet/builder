package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

public class VnFrame implements Insertable, Comparable<VnFrame>
{
	public static final Comparator<VnFrame> COMPARATOR = Comparator //
			.comparing(VnFrame::getName) //
			.thenComparing(VnFrame::getSubName, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(VnFrame::getDescriptionNumber) //
			.thenComparing(VnFrame::getDescriptionXTag) //
			.thenComparing(VnFrame::getSyntax) //
			.thenComparing(VnFrame::getSemantics);

	public static final SetCollector<VnFrame> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String descriptionNumber;

	private final String descriptionXTag;

	private final VnFrameName name;

	private final VnFrameSubName subName;

	private final VnSyntax syntax;

	private final VnSemantics semantics;

	// C O N S T R U C T

	public static VnFrame make(final String descriptionNumber, final String descriptionXTag, final String descriptionPrimary, final String descriptionSecondary, final String syntax, final String semantics) throws ParserConfigurationException, SAXException, IOException
	{
		var f = new VnFrame(descriptionNumber, descriptionXTag, descriptionPrimary, descriptionSecondary, syntax, semantics);
		COLLECTOR.add(f);
		return f;
	}

	private VnFrame(final String descriptionNumber, final String descriptionXTag, final String descriptionPrimary, final String descriptionSecondary, final String syntax, final String semantics) throws ParserConfigurationException, SAXException, IOException
	{
		this.descriptionNumber = descriptionNumber;
		this.descriptionXTag = descriptionXTag;
		this.name = VnFrameName.make(descriptionPrimary);
		this.subName = descriptionSecondary == null || descriptionSecondary.isEmpty() ? null : VnFrameSubName.make(descriptionSecondary);
		this.syntax = VnSyntax.make(syntax);
		this.semantics = VnSemantics.make(semantics);
	}

	// A C C E S S

	public VnFrameName getName()
	{
		return name;
	}

	public VnFrameSubName getSubName()
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

	public VnSyntax getSyntax()
	{
		return syntax;
	}

	public VnSemantics getSemantics()
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
		VnFrame that = (VnFrame) o;
		return descriptionNumber.equals(that.descriptionNumber) && descriptionXTag.equals(that.descriptionXTag) && name.equals(that.name) && Objects.equals(subName, that.subName) && Objects.equals(syntax, that.syntax) && Objects.equals(semantics, that.semantics);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(descriptionNumber, descriptionXTag, name, subName, syntax, semantics);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnFrame that)
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

	@RequiresIdFrom(type = VnFrameName.class)
	@RequiresIdFrom(type = VnFrameSubName.class)
	@RequiresIdFrom(type = VnSyntax.class)
	@RequiresIdFrom(type = VnSemantics.class)
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
				VnFrameName.COLLECTOR.get(name), //
				VnFrameSubName.COLLECTOR.get(subName), //
				VnSyntax.COLLECTOR.get(syntax), //
				VnSemantics.COLLECTOR.get(semantics));
	}
}
