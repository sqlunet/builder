package org.sqlbuilder.common;

import java.util.Locale;
import java.util.function.Function;

public class Utils
{
	private static final String NULLSTR = "NULL";

	private static final String QUOTE = "'";

	private static final String ESCAPED_QUOTE = "''";

	private static final char BACKTICK = '`';

	// Q U O T E

	/**
	 * Quote string for it to be handled by SQL
	 *
	 * @param str string
	 * @return SQL quoted string
	 */
	public static String quote(final String str)
	{
		return QUOTE + str + QUOTE;
	}

	// E S C A P E

	/**
	 * Escape string for it to be handled by SQL
	 *
	 * @param str string
	 * @return SQL escaped string
	 */
	public static String escape(final String str)
	{
		return str.replace(QUOTE, ESCAPED_QUOTE);
	}

	// N U L L A B L E

	/**
	 * Stringify nullable object for it to be handled by SQL
	 *
	 * @param object object
	 * @return SQL string
	 */
	public static <T> String nullable(final T object)
	{
		return object == null ? NULLSTR : object.toString();
	}

	/**
	 * Stringify nullable object for it to be handled by SQL
	 *
	 * @param object   object
	 * @param toString stringifier
	 * @return SQL string
	 */
	public static <T> String nullable(final T object, final Function<T, String> toString)
	{
		return object == null ? NULLSTR : toString.apply(object);
	}

	// N U L L A B L E   +   Q U O T E D   +   E S C  A P E D

	/**
	 * Nullable quoted object string value for it to be handled by SQL
	 *
	 * @param object   object
	 * @param toString string function
	 * @return SQL string
	 */
	public static <T> String nullableQuotedString(final T object, final Function<T, String> toString)
	{
		return nullable(object, o -> quote(toString.apply(o)));
	}

	/**
	 * Nullable quoted escaped object string value for it to be handled by SQL
	 *
	 * @param object   object
	 * @param toString string function
	 * @return SQL string
	 */
	public static <T> String nullableQuotedEscapedString(final T object, final Function<T, String> toString)
	{
		return nullable(object, o -> quote(escape(toString.apply(o))));
	}

	/**
	 * Nullable quoted object string value for it to be handled by SQL
	 *
	 * @param object object
	 * @return SQL string
	 */
	public static <T> String nullableQuotedString(final T object)
	{
		return nullableQuotedString(object, Object::toString);
	}

	/**
	 * Nullable quoted escaped object string value for it to be handled by SQL
	 *
	 * @param object object
	 * @return SQL non-escaped string or NULL
	 */
	public static <T> String nullableQuotedEscapedString(final T object)
	{
		return nullableQuotedEscapedString(object, Object::toString);
	}

	// C H A R  A N D   I N T

	/**
	 * Nullable character for it to be handled by SQL
	 *
	 * @param c character
	 * @return SQL char or NULL
	 */
	public static String nullableQuotedChar(final Character c)
	{
		return nullable(c, o -> quote(c.toString()));
	}

	/**
	 * Nullable int for it to be handled by SQL
	 *
	 * @param i int
	 * @return SQL int or NULL
	 */
	public static String nullableInt(final Integer i)
	{
		return nullable(i, String::valueOf);
	}

	/**
	 * Nullable int for it to be handled by SQL
	 *
	 * @param f float
	 * @return SQL float or NULL
	 */
	public static String nullableFloat(final Float f)
	{
		return nullable(f, String::valueOf);
	}

	//

	/**
	 * Escape zeroable int for it to be handled by SQL, zero interpreted as NULL
	 *
	 * @param i int
	 * @return SQL escaped string or NULL
	 */
	public static String zeroableInt(final int i)
	{
		return i == 0 ? NULLSTR : String.valueOf(i);
	}

	/**
	 * Back tick
	 *
	 * @param value value to backtick
	 * @return backticked string value
	 */
	public static String backtick(final String value)
	{
		return BACKTICK + value + BACKTICK;
	}

	/**
	 * Camel-case string
	 *
	 * @param str str
	 * @return camel-cased string
	 */
	public static String camelCase(final String str)
	{
		if (str != null && !str.isEmpty())
		{
			return str.substring(0, 1).toUpperCase(Locale.ENGLISH) + str.substring(1).toLowerCase(Locale.ENGLISH);
		}
		return str;
	}
}
