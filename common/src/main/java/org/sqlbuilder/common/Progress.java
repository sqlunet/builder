package org.sqlbuilder.common;

public class Progress
{
	public static void tracePending(final String tag, final String message)
	{
		System.out.print(tag + " " + message);
	}

	public static void traceDone()
	{
		traceDone(null);
	}

	public static void traceDone(final String message)
	{
		if (message == null)
		{
			System.out.println(" ✓");
		}
		else
		{
			System.err.println(" ✘ " + message);
		}
	}

	public static void traceHeader(final String tag, final String message)
	{
		System.err.println(">" + tag + " " + message);
	}

	public static void traceTailer(final String tag, final String message)
	{
		System.err.println("<" + tag + " " + message);
	}

	public static void trace(final String tag, final String message)
	{
		System.err.println(tag + " " + message);
	}

	public static void trace(final String message)
	{
		System.err.println("mess: " + message);
	}

	private static final long GRANULARITY = 10;

	private static final long PERLINE = 100;

	public static void trace(final long count)
	{
		if (count % GRANULARITY == 0)
		{
			System.err.print('.');
		}
		if (count % (GRANULARITY * PERLINE) == 0)
		{
			System.err.print('\n');
		}
	}

	public static void traceTailer(final long count)
	{
		System.err.println("<" + count);
	}

	public static void info(final String message)
	{
		System.err.println(message);
	}
}
