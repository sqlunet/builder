package org.sqlbuilder.fn;

public class Util
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
