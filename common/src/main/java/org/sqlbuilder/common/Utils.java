package org.sqlbuilder.common;

public class Utils
{
	/**
	 * Escape nullable string for it to be handled by SQL
	 *
	 * @param o object
	 * @return string or "NULL"
	 */
	public static String nullableObject(final Object o)
	{
		return o == null ? "NULL" : o.toString();
	}

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
	 * @param str string
	 * @return SQL escaped string or "NULL"
	 */
	public static String nullableString(final String str)
	{
		return str == null ? "NULL" : "'" + escape(str) + "'";
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
	 * Escape nullable int for it to be handled by SQL
	 *
	 * @param l long
	 * @return SQL long or "NULL"
	 */
	public static String nullableLong(final Long l)
	{
		return l == null ? "NULL" : String.valueOf(l);
	}

	/**
	 * Escape zeroable long for it to be handled by SQL
	 *
	 * @param i long
	 * @return SQL escaped string or "NULL"
	 */
	public static String zeroableLong(final long i)
	{
		return i == 0 ? "NULL" : String.valueOf(i);
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
