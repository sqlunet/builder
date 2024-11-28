package org.sqlbuilder.common

import java.util.*

object Utils {

    private const val NULLSTR = "NULL"

    private const val QUOTE = "'"

    private const val ESCAPED_QUOTE = "''"

    private const val BACKTICK = '`'

    // Q U O T E

    /**
     * Quote string for it to be handled by SQL
     *
     * @param str string
     * @return SQL quoted string
     */
    @JvmStatic
    fun quote(str: String): String {
        return QUOTE + str + QUOTE
    }

    // E S C A P E

    /**
     * Escape string for it to be handled by SQL
     *
     * @param str string
     * @return SQL escaped string
     */
    @JvmStatic
    fun escape(str: String): String {
        return str.replace(QUOTE, ESCAPED_QUOTE)
    }

    // N U L L A B L E

    /**
     * Stringify nullable object for it to be handled by SQL
     *
     * @param obj object
     * @return SQL string
     */
    @JvmStatic
    fun <T> nullable(obj: T?): String {
        return obj?.toString() ?: NULLSTR
    }

    /**
     * Stringify nullable object for it to be handled by SQL
     *
     * @param obj object
     * @param toString stringifier
     * @return SQL string
     */
    @JvmStatic
    fun <T> nullable(obj: T?, toString: (T) -> String): String {
        return if (obj == null) NULLSTR else toString(obj)
    }

    // Q U O T E D   +   E S C A P E D

    /**
     * Quoted object string value for it to be handled by SQL
     *
     * @param obj object
     * @return SQL string
     */
    @JvmStatic
    fun <T> quotedEscapedString(obj: T): String {
        return quotedEscapedString(obj) { it.toString() }
    }

    /**
     * Quoted object string value for it to be handled by SQL
     *
     * @param obj object
     * @param toString string function
     * @return SQL string
     */
    @JvmStatic
    fun <T> quotedEscapedString(obj: T, toString: (T) -> String): String {
        return quote(escape(toString(obj)))
    }

    // N U L L A B L E   +   Q U O T E D   +   E S C A P E D

    /**
     * Nullable quoted object string value for it to be handled by SQL
     *
     * @param obj   object
     * @param toString string function
     * @return SQL string
     */
    @JvmStatic
    fun <T> nullableQuotedString(obj: T?, toString: (T) -> String): String {
        return nullable(obj) { quote(toString(it)) }
    }

    /**
     * Nullable quoted escaped object string value for it to be handled by SQL
     *
     * @param obj   object
     * @param toString string function
     * @return SQL string
     */
    @JvmStatic
    fun <T> nullableQuotedEscapedString(obj: T?, toString: (T) -> String): String {
        return nullable(obj) { quote(escape(toString(it))) }
    }

    /**
     * Nullable quoted object string value for it to be handled by SQL
     *
     * @param obj object
     * @return SQL string
     */
    @JvmStatic
    fun <T> nullableQuotedString(obj: T?): String {
        return nullableQuotedString(obj) { it.toString() }
    }

    /**
     * Nullable quoted escaped object string value for it to be handled by SQL
     *
     * @param obj object
     * @return SQL non-escaped string or NULL
     */
    @JvmStatic
    fun <T> nullableQuotedEscapedString(obj: T?): String {
        return nullableQuotedEscapedString(obj) { it.toString() }
    }

    // C H A R  A N D   I N T

    /**
     * Nullable character for it to be handled by SQL
     *
     * @param c character
     * @return SQL char or NULL
     */
    @JvmStatic
    fun nullableQuotedChar(c: Char?): String {
        return nullable(c) { quote(it.toString()) }
    }

    /**
     * Nullable int for it to be handled by SQL
     *
     * @param i int
     * @return SQL int or NULL
     */
    @JvmStatic
    fun nullableInt(i: Int?): String {
        return nullable(i) { it.toString() }
    }

    /**
     * Nullable int for it to be handled by SQL
     *
     * @param l long
     * @return SQL long or NULL
     */
    @JvmStatic
    fun nullableLong(l: Long?): String {
        return nullable(l) { it.toString() }
    }

    /**
     * Nullable float for it to be handled by SQL
     *
     * @param f float
     * @return SQL float or NULL
     */
    @JvmStatic
    fun nullableFloat(f: Float?): String {
        return nullable(f) { it.toString() }
    }

    /**
     * Nullable date for it to be handled by SQL
     *
     * @param date date
     * @return SQL timestamp or NULL
     */
    @JvmStatic
    fun nullableDate(date: Date?): String {
        return nullable(date) { it.time.toString() }
    }

    //
    /**
     * Escape zeroable int for it to be handled by SQL, zero interpreted as NULL
     *
     * @param i int
     * @return SQL escaped string or NULL
     */
    @JvmStatic
    fun zeroableInt(i: Int): String {
        return if (i == 0) NULLSTR else i.toString()
    }

    /**
     * Back tick
     *
     * @param value value to backtick
     * @return backticked string value
     */
    @JvmStatic
    fun backtick(value: String): String {
        return "${BACKTICK}$value${BACKTICK}"
    }

    /**
     * Camel-case string
     *
     * @param str str
     * @return camel-cased string
     */
    @JvmStatic
    fun camelCase(str: String): String {
        if (!str.isEmpty()) {
            return "${str.substring(0, 1).uppercase()}${str.substring(1).lowercase()}"
        }
        return str
    }
}
