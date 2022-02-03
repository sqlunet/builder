package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.vn.objects.Frame;
import org.sqlbuilder.vn.objects.FrameExample;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class VnFrameExampleMapping implements Insertable, Comparable<VnFrameExampleMapping>
{
	public static final Set<VnFrameExampleMapping> SET = new HashSet<>();

	private final Frame frame;

	private final FrameExample example;

	// C O N S T R U C T

	public VnFrameExampleMapping(final Frame frame, final FrameExample example)
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

	@RequiresIdFrom(type = Frame.class)
	@RequiresIdFrom(type = FrameExample.class)
	@Override
	public String dataRow()
	{
		// frame.id
		// example.id
		return String.format("%d,%d", Frame.COLLECTOR.get(frame), FrameExample.COLLECTOR.get(example));
	}
}
