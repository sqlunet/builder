package org.sqlbuilder.fn.types;

import org.sqlbuilder.fn.Collector;

import java.util.Comparator;

public class FrameRelation
{
	// framerelations.table=fnframerelations
	// framerelations.create=CREATE TABLE IF NOT EXISTS %Fn_framerelations.table% ( relationid INTEGER NOT NULL AUTO_INCREMENT,relation VARCHAR(20) DEFAULT NULL,PRIMARY KEY (relationid) );
	// framerelations.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_framerelations.table%_relation ON %Fn_framerelations.table% (relation);
	// framerelations.no-unq1=DROP INDEX IF EXISTS unq_%Fn_framerelations.table%_relation;

	public static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	public static final Collector<String> COLLECTOR = new Collector<>(COMPARATOR);

	public static void add(String type)
	{
		COLLECTOR.add(type);
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
}
