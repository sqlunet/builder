package org.sqlbuilder.common;

public class SqlId
{
	public static Object getSqlId(Integer id)
	{
		if (id != null)
		{
			return id;
		}
		return "NULL";
	}
}
