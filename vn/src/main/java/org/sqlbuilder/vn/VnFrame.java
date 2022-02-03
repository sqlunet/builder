package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

public class VnFrame implements Insertable, Comparable<VnFrame>
{
	protected static final Set<VnFrame> SET = new HashSet<>();

	public static Map<VnFrame, Integer> MAP;

	private final String descriptionNumber;

	private final String descriptionXTag;

	private final VnFrameName name;

	private final VnFrameSubName subName;

	private final VnSyntax syntax;

	private final VnSemantics semantics;

	// C O N S T R U C T

	public VnFrame(final String descriptionNumber, final String descriptionXTag, final String descriptionPrimary, final String descriptionSecondary, final String syntax, final String semantics) throws ParserConfigurationException, SAXException, IOException
	{
		this.descriptionNumber = descriptionNumber;
		this.descriptionXTag = descriptionXTag;
		this.name = new VnFrameName(descriptionPrimary);
		this.subName = descriptionSecondary == null || descriptionSecondary.isEmpty() ? null : new VnFrameSubName(descriptionSecondary);
		this.syntax = new VnSyntax(syntax);
		this.semantics = new VnSemantics(semantics);
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
		// name
		int cmp = name.compareTo(that.name);
		if (cmp != 0)
		{
			return cmp;
		}

		// subname
		if (subName == null)
		{
			if (that.subName == null)
			{
				cmp = 0; // null * null
			}
			else
			{
				cmp = -1; // null * x
			}
		}
		else
		{
			if (that.subName == null)
			{
				cmp = 1; // x * null
			}
			else
			{
				cmp = subName.compareTo(that.subName); // x x
			}
		}
		if (cmp != 0)
		{
			return cmp;
		}
		cmp = that.subName == null ? 0 : that.subName.compareTo(subName);
		if (cmp != 0)
		{
			return cmp;
		}

		// description number
		cmp = this.descriptionNumber.compareTo(that.descriptionNumber);
		if (cmp != 0)
		{
			return cmp;
		}

		// description xtag
		cmp = this.descriptionXTag.compareTo(that.descriptionXTag);
		if (cmp != 0)
		{
			return cmp;
		}

		// syntax id
		cmp = syntax.compareTo(that.syntax);
		if (cmp != 0)
		{
			return cmp;
		}

		// semantics id
		cmp = semantics.compareTo(that.semantics);
		return cmp;
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return " number=" + this.descriptionNumber + " tag=" + this.descriptionXTag + " description1=" + this.name + " description2=" + this.subName + " syntax=" + this.syntax + " semantics=" + this.semantics;
	}

	// I N S E R T

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
				VnFrameName.MAP.get(name), //
				VnFrameSubName.MAP.get(subName), //
				VnSyntax.MAP.get(syntax), //
				VnSemantics.MAP.get(semantics));
	}
}
