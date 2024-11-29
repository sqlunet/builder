package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.vn.objects.Frame;
import org.sqlbuilder.vn.objects.VnClass;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Class_Frame implements Insertable, Comparable<Class_Frame>
{
	public static final Comparator<Class_Frame> COMPARATOR = Comparator.comparing(Class_Frame::getClazz).thenComparing(Class_Frame::getFrame);

	public static final Set<Class_Frame> SET = new HashSet<>();

	private final VnClass clazz;

	private final Frame frame;

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static Class_Frame make(final VnClass clazz, final Frame frame)
	{
		var m = new Class_Frame(clazz, frame);
		SET.add(m);
		return m;
	}

	private Class_Frame(final VnClass clazz, final Frame frame)
	{
		this.frame = frame;
		this.clazz = clazz;
	}

	// A C C E S S

	public VnClass getClazz()
	{
		return clazz;
	}

	public Frame getFrame()
	{
		return frame;
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
		Class_Frame that = (Class_Frame) o;
		return clazz.equals(that.clazz) && frame.equals(that.frame);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(clazz, frame);
	}

	// O R D E R

	@Override
	public int compareTo(final Class_Frame that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = Frame.class)
	@Override
	public String dataRow()
	{
		return String.format("%d,%d", clazz.getIntId(), frame.getIntId());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", clazz.name, frame.getName());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return "class=" + clazz + " frame=" + frame;
	}
}
