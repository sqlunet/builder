package org.sqlbuilder.fn.types;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.common.SqlId;

import java.util.Comparator;

public class FrameRelation
{
	public static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	public static final SetCollector<String> COLLECTOR = new SetCollector<>(COMPARATOR);

	public static void add(String type)
	{
		COLLECTOR.add(type);
	}

	@RequiresIdFrom(type = FrameRelation.class)
	public static Integer getIntId(String value)
	{
		return value == null ? null : COLLECTOR.apply(value);
	}

	@RequiresIdFrom(type = FrameRelation.class)
	public static Object getSqlId(String value)
	{
		return SqlId.getSqlId(getIntId(value));
	}
}

/*
# relationid, relation
1, Has Subframe(s)
2, Inherits from
3, Is Causative of
4, Is Inchoative of
5, Is Inherited by
6, Is Perspectivized in
7, Is Preceded by
8, Is Used by
9, Perspective on
10, Precedes
11, See also
12, Subframe of
13, Uses
*/
