package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class VnFrameSubName implements Insertable<VnFrameSubName>, Comparable<VnFrameSubName>
{
	protected static final SortedSet<VnFrameSubName> SET = new TreeSet<>();

	public static Map<VnFrameSubName, Integer> MAP;

	private final String subName;

	// C O N S T R U C T

	public VnFrameSubName(final String name)
	{
		this.subName = name.trim().toUpperCase().replaceAll("\\s+", " ");
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
		return this.subName.compareTo(that.subName);
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
