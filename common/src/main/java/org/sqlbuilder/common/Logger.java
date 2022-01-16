package org.sqlbuilder.common;

import java.sql.SQLException;

public class Logger
{
	public static boolean verbose = false;
	public static Logger instance = new Logger();


	public void logParseException(final String moduleId, final String tag, final String message, final String filename, final long lineCount, final String line, final Object o, final ParseException pe)
	{
	}

	public void logNotFoundException(final String moduleId, final String tag, final String message, final String filename, final long lineCount, final String line, final Object o, final NotFoundException wnfe)
	{
	}

	public void logSQLUpdateException(final String moduleId, final String tag, final String message, final String filename, final long lineCount, final String line, final String s, final SQLUpdateException sqlue)
	{
	}

	public void logXmlException(final String moduleId, final String tag, final String message, final String filename, final int lineCount, final Object o, final String s, final Exception e)
	{
	}

	public void logSQLException(final String moduleId, final String tag, final String message, final String fileName, final int lineCount, final Object o, final String s, final SQLException sqlue)
	{
	}

	public void logNotFoundExceptionSilently(final String moduleId, final String tag, final String message, final String fileName, final int lineCount, final Object o, final String s, final NotFoundException nfe)
	{
	}
}
