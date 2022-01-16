package org.sqlbuilder.common;

import java.io.IOException;

public abstract class Processor
{
	protected final String tag;

	public Processor(final String tag)
	{
		this.tag = tag;
	}

	protected abstract void run() throws IOException;
}
