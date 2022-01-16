package org.sqlbuilder.common;

public interface Insertable<T>
{
	String dataRow();

	default String comment()
	{
		return null;
	}
}
