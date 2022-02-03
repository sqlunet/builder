package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class VnFrameExampleMapping implements Insertable, Comparable<VnFrameExampleMapping>
{
	protected static final Set<VnFrameExampleMapping> SET = new HashSet<>();

	private final VnFrame frame;

	private final VnFrameExample example;

	// C O N S T R U C T

	public VnFrameExampleMapping(final VnFrame frame, final VnFrameExample example)
	{
		this.frame = frame;
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
		VnFrameExampleMapping that = (VnFrameExampleMapping) o;
		return frame.equals(that.frame) && example.equals(that.example);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(frame, example);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnFrameExampleMapping that)
	{
		int cmp = this.frame.compareTo(that.frame);
		if (cmp != 0)
		{
			return cmp;
		}
		return this.example.compareTo(that.example);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// frame.id
		// example.id
		return String.format("%d,%d", VnFrame.MAP.get(frame), VnFrameExample.MAP.get(example));
	}
}
