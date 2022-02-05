package org.sqlbuilder.common;

public interface HasId
{
	default String getSqlId()
	{
		Integer id = getIntId();
		if (id != null)
		{
			return id.toString();
		}
		return "NULL";
	}

	Integer getIntId();
}
