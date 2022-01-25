package org.sqlbuilder.fn.types;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
fetypes.table=fnfetypes
fetypes.create=CREATE TABLE IF NOT EXISTS %Fn_fetypes.table% ( fetypeid INTEGER NOT NULL AUTO_INCREMENT,fetype VARCHAR(30),PRIMARY KEY (fetypeid) );
fetypes.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_fetypes.table%_fetype ON %Fn_fetypes.table% (fetype);
fetypes.no-unq1=DROP INDEX IF EXISTS unq_%Fn_fetypes.table%_fetype;
 */
public class FeType
{

	public static final Set<String> SET = new HashSet<>();

	public static Map<String, Integer> MAP;

	public static void add(String type)
	{
		SET.add(type);
	}

	public static int getIntId(String value)
	{
		if (value != null)
		{
			Integer id = MAP.get(value);
			if (id != null)
			{
				return id;
			}
		}
		throw new IllegalArgumentException(value);
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
# fetypeid, fetype
1, Abundant_entities
2, Abuser
3, Accessibility
4, Accessory
5, Accoutrement
6, Accuracy
7, Accused
8, Act
9, Action
 */
}
