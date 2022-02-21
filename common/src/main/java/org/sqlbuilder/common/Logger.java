package org.sqlbuilder.common;

public class Logger
{
	public static boolean verbose = false;

	public static Logger instance = new Logger();

	public void logParseException(final String moduleId, final String tag, final String filename, final long lineCount, final String line, final ParseException pe)
	{
		System.err.printf("%s %s %s:%d%s %s%n", moduleId, tag, filename, lineCount, line == null ? "" : String.format("='%s'", line), pe.getMessage());
	}

	public void logNotFoundException(final String moduleId, final String tag, final String filename, final long lineCount, final String line, final NotFoundException nfe)
	{
		System.err.printf("%s %s %s:%d%s %s%n", moduleId, tag, filename, lineCount, line == null ? "" : String.format("='%s'", line), nfe.getMessage());
	}

	public void logXmlException(final String moduleId, final String tag, final String where, final Exception e)
	{
		System.err.printf("%s %s '%s' %s%n", moduleId, tag, where, e.getMessage());
	}

	public void logWarn(final String moduleId, final String tag, final String tag2, final String message)
	{
		System.err.printf("%s %s %s %s%n", moduleId, tag, tag2, message);
	}
}
