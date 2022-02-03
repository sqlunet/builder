package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.Objects;

public class FrameExample implements Insertable, Comparable<FrameExample>
{
	public static final Comparator<FrameExample> COMPARATOR = Comparator.comparing(FrameExample::getExample);

	public static final SetCollector<FrameExample> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String example;

	// C O N S T R U C T

	public static FrameExample make(final String example)
	{
		var e = new FrameExample(example);
		COLLECTOR.add(e);
		return e;
	}

	private FrameExample(final String example)
	{
		this.example = example;
	}

	// A C C E S S

	public String getExample()
	{
		return example;
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
		FrameExample that = (FrameExample) o;
		return example.equals(that.example);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(example);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final FrameExample that)
	{
		return COMPARATOR.compare(this, that);
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
