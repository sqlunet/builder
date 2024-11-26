package org.sqlbuilder.fn.types;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.common.SetCollector2;
import org.sqlbuilder.common.SqlId;

import java.util.Comparator;

public class GfType
{
	public static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	public static final SetCollector2<String> COLLECTOR = new SetCollector2<>(COMPARATOR);

	@RequiresIdFrom(type = GfType.class)
	public static Integer getIntId(String value)
	{
		return value == null ? null : COLLECTOR.apply(value);
	}

	@RequiresIdFrom(type = GfType.class)
	public static Object getSqlId(String value)
	{
		return SqlId.getSqlId(getIntId(value));
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

