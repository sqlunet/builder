package org.sqlbuilder.common;

public class AlreadyFoundException extends RuntimeException
{
	public AlreadyFoundException(final String message)
	{
		super(message);
	}
}
