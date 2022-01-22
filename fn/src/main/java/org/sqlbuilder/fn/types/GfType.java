package org.sqlbuilder.fn.types;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GfType
{
	// gftypes.table=fngftypes
	// gftypes.create=CREATE TABLE IF NOT EXISTS %Fn_gftypes.table% ( gfid INTEGER NOT NULL AUTO_INCREMENT,gf VARCHAR(10),PRIMARY KEY (gfid) );
	// gftypes.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_gftypes.table%_gf ON %Fn_gftypes.table% (gf);
	// gftypes.no-unq1=DROP INDEX IF EXISTS unq_%Fn_gftypes.table%_gf;

	public static final Set<String> SET = new HashSet<>();

	public static Map<String, Integer> MAP;

	public static void add(String type)
	{
		SET.add(type);
	}

	public static Object getId(String value)
	{
		if (value != null)
		{
			Integer id = MAP.get(value);
			if (id != null)
			{
				return id;
			}
		}
		return "NULL";
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
}
