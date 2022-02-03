package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class VnFrameExample implements Insertable, Comparable<VnFrameExample>
{
	protected static final Set<VnFrameExample> SET = new HashSet<>();

	public static Map<VnFrameExample, Integer> MAP;

	private final String example;

	// C O N S T R U C T

	public VnFrameExample(final String example)
	{
		this.example = example;
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
		VnFrameExample that = (VnFrameExample) o;
		return example.equals(that.example);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(example);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnFrameExample that)
	{
		return this.example.compareTo(that.example);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// id
		// example
		return String.format("'%s'", Utils.escape(example));
	}
}
