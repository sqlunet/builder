package org.sqlbuilder.fn.types;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.common.SqlId;

import java.util.Comparator;

public class Status
{
	public static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	public static final SetCollector<String> COLLECTOR = new SetCollector<>(COMPARATOR);

	public static void add(String value)
	{
		COLLECTOR.add(value);
	}

	@RequiresIdFrom(type = Status.class)
	public static Integer getIntId(String value)
	{
		return value == null ? null : COLLECTOR.get(value);
	}

	@RequiresIdFrom(type = Status.class)
	public static Object getSqlId(String value)
	{
		return SqlId.getSqlId(getIntId(value));
	}
}

/*
# statusid, status
1, Add_Annotation
2, BTDT
3, Created
4, Finished_Initial
5, Finished_X-Gov
6, FN1_Sent
7, Insufficient_Attestations
8, In_Use
9, Needs_SCs
10, New
11, Pre-Marked
12, Rules_Defined
13, AUTO_APP
14, AUTO_EDITED
15, MANUAL
16, UNANN
*/

