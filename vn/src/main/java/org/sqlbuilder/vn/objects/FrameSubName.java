package org.sqlbuilder.vn.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;

import java.util.Comparator;

public class FrameSubName implements HasId, Insertable, Comparable<FrameSubName>
{
	public static final Comparator<FrameSubName> COMPARATOR = Comparator.comparing(FrameSubName::getSubName);

	public static final SetCollector<FrameSubName> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String subName;

	// C O N S T R U C T O R

	public static FrameSubName make(final String subname)
	{
		var s = new FrameSubName(subname);
		COLLECTOR.add(s);
		return s;
	}

	private FrameSubName(final String subname)
	{
		this.subName = subname.trim().toUpperCase().replaceAll("\\s+", " ");
	}

	// A C C E S S

	public String getSubName()
	{
		return subName;
	}

	@Override
	public Integer getIntId()
	{
		return COLLECTOR.apply(this);
	}

	@RequiresIdFrom(type = FrameSubName.class)
	public static Integer getIntId(final FrameSubName subname)
	{
		return subname == null ? null : COLLECTOR.apply(subname);
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof FrameSubName))
		{
			return false;
		}
		final FrameSubName that = (FrameSubName) o;
		return this.subName.equals(that.subName);
	}

	@Override
	public int hashCode()
	{
		return this.subName.hashCode();
	}

	// O R D E R I N G

	@Override
	public int compareTo(@NotNull final FrameSubName that)
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
