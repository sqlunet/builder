package org.sqlbuilder.common;

public class Utils
{
	/**
	 * Escape string for it to be handled by SQL
	 *
	 * @param str string
	 * @return SQL escaped string
	 */
	public static String escape(final String str)
	{
		return str.replace("'", "''");
	}
}
