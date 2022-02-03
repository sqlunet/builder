package org.sqlbuilder.common;

public interface Insertable
{
	String dataRow();

	default String comment()
	{
		return null;
	}
}
