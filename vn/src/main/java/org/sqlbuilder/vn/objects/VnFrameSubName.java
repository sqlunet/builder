package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;

import java.util.Comparator;

public class VnFrameSubName implements Insertable, Comparable<VnFrameSubName>
{
	public static final Comparator<VnFrameSubName> COMPARATOR = Comparator.comparing(VnFrameSubName::getSubName);

	public static final SetCollector<VnFrameSubName> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String subName;

	// C O N S T R U C T

	public static VnFrameSubName make(final String subname)
	{
		var s = new VnFrameSubName(subname);
		COLLECTOR.add(s);
		return s;
	}

	private VnFrameSubName(final String subname)
	{
		this.subName = subname.trim().toUpperCase().replaceAll("\\s+", " ");
	}

	// A C C E S S

	public String getSubName()
	{
		return subName;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof VnFrameSubName))
		{
			return false;
		}
		final VnFrameSubName that = (VnFrameSubName) o;
		return this.subName.equals(that.subName);
	}

	@Override
	public int hashCode()
	{
		return this.subName.hashCode();
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnFrameSubName that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// id
		// subName
		return String.format("'%s'", subName);
	}
}
