package org.semantikos.common;

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
