package org.semantikos.common;

public interface Insertable
{
	String dataRow();

	default String comment()
	{
		return null;
	}
}
