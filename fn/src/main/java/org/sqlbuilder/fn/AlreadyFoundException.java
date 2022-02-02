package org.sqlbuilder.fn;

public class AlreadyFoundException extends RuntimeException
{
	public AlreadyFoundException()
	{
	}

	public AlreadyFoundException(final String message)
	{
		super(message);
	}

	public AlreadyFoundException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public AlreadyFoundException(final Throwable cause)
	{
		super(cause);
	}

	public AlreadyFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
