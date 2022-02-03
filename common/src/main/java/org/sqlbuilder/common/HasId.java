package org.sqlbuilder.common;

public interface HasId
{
	default Object getSqlId()
	{
		Integer id = getIntId();
		if (id != null)
		{
			return id;
		}
		return "NULL";
	}

	Integer getIntId();
}
