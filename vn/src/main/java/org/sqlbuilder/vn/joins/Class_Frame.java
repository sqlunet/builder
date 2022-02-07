package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.vn.objects.Frame;
import org.sqlbuilder.vn.objects.VnClass;

import java.util.HashSet;
import java.util.Set;

public class Class_Frame implements Insertable
{
	public static final Set<Class_Frame> SET = new HashSet<>();

	private final VnClass clazz;

	private final Frame frame;

	// C O N S T R U C T

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

	// I N S E R T

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = Frame.class)
	@Override
	public String dataRow()
	{
		return String.format("%d,%d",
				clazz.getIntId(),
				frame.getIntId());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s",
				clazz.getName(),
				frame.getName());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return "class=" + clazz + " frame=" + frame;
	}
}
