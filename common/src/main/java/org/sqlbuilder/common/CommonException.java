package org.sqlbuilder.common;

public class CommonException extends Exception
{
	public CommonException()
	{
	}

	public CommonException(final String message)
	{
		super(message);
	}

	public CommonException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public CommonException(final Throwable cause)
	{
		super(cause);
	}

	public CommonException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
