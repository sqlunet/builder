package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.vn.objects.Frame;
import org.sqlbuilder.vn.objects.FrameExample;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Frame_Example implements Insertable, Comparable<Frame_Example>
{
	public static final Comparator<Frame_Example> COMPARATOR = Comparator.comparing(Frame_Example::getFrame).thenComparing(Frame_Example::getExample);

	public static final Set<Frame_Example> SET = new HashSet<>();

	private final Frame frame;

	private final FrameExample example;

	// C O N S T R U C T O R
	public static Frame_Example make(final Frame frame, final FrameExample example)
	{
		var m = new Frame_Example(frame, example);
		SET.add(m);
		return m;
	}

	private Frame_Example(final Frame frame, final FrameExample example)
	{
		this.frame = frame;
		this.example = example;
	}

	// A C C E S S

	public Frame getFrame()
	{
		return frame;
	}

	public FrameExample getExample()
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
		Frame_Example that = (Frame_Example) o;
		return frame.equals(that.frame) && example.equals(that.example);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(frame, example);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Frame_Example that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = Frame.class)
	@RequiresIdFrom(type = FrameExample.class)
	@Override
	public String dataRow()
	{
		// frame.id
		// example.id
		return String.format("%d,%d", frame.getIntId(), example.getIntId());
	}
}
