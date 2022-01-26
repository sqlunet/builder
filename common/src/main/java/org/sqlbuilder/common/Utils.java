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

	/**
	 * Escape nullable string for it to be handled by SQL
	 *
	 * @param object string
	 * @return SQL escaped string or "NULL"
	 */
	public static String nullableEscapedString(final Object object)
	{
		return object == null ? "NULL" : "'" + escape(object.toString()) + "'";
	}

	/**
	 * Escape nullable string for it to be handled by SQL
	 *
	 * @param object string
	 * @return SQL non-escaped string or "NULL"
	 */
	public static String nullableString(final Object object)
	{
		return object == null ? "NULL" : "'" + object.toString() + "'";
	}

	/**
	 * Escape nullable character for it to be handled by SQL
	 *
	 * @param c character
	 * @return SQL char or "NULL"
	 */
	public static String nullableChar(final Character c)
	{
		return c == null ? "NULL" : "'" + String.valueOf(c) + "'";
	}

	/**
	 * Escape nullable int for it to be handled by SQL
	 *
	 * @param i int
	 * @return SQL int or "NULL"
	 */
	public static String nullableInt(final Integer i)
	{
		return i == null ? "NULL" : String.valueOf(i);
	}

	/**
	 * Escape zeroable int for it to be handled by SQL
	 *
	 * @param i int
	 * @return SQL escaped string or "NULL"
	 */
	public static String zeroableInt(final int i)
	{
		return i == 0 ? "NULL" : String.valueOf(i);
	}
}
