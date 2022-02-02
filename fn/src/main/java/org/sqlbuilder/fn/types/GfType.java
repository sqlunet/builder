package org.sqlbuilder.fn.types;

import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.SetCollector;
import org.sqlbuilder.fn.Util;

import java.util.Comparator;

public class GfType
{
	public static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	public static final SetCollector<String> COLLECTOR = new SetCollector<>(COMPARATOR);

	@RequiresIdFrom(type = GfType.class)
	public static Integer getIntId(String value)
	{
		return value == null ? null : COLLECTOR.get(value);
	}

	@RequiresIdFrom(type = GfType.class)
	public static Object getSqlId(String value)
	{
		return Util.getSqlId(getIntId(value));
	}
}

/*
# gfid, gf
1, NULL
2, Appositive
3, Dep
4, Ext
5, Gen
6, Head
7, Obj
8, Quant
*/

