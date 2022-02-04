package org.sqlbuilder.common;

public class IgnoreException extends Exception
{
	public IgnoreException()
	{
	}

	public IgnoreException(String message)
	{
		super(message);
	}

	public IgnoreException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public IgnoreException(final Throwable cause)
	{
		super(cause);
	}
}
