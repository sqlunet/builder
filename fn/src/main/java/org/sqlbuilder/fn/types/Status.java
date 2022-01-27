package org.sqlbuilder.fn.types;

import org.sqlbuilder.fn.Collector;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Status
{
	// statuses.table=fnstatuses
	// statuses.create=CREATE TABLE IF NOT EXISTS %Fn_statuses.table% ( statusid INTEGER NOT NULL,status VARCHAR(32),PRIMARY KEY (statusid) );

	public static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	public static final Collector<String> COLLECTOR = new Collector<>(COMPARATOR);

	public static void add(String value)
	{
		COLLECTOR.add(value);
	}

	public static Object getId(String value)
	{
		Integer id = COLLECTOR.get(value);
		if (id != null)
		{
			return id;
		}
		return "NULL";
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
}
