package org.sqlbuilder.fn;

import java.io.File;
import java.sql.Connection;
import java.util.Properties;

import org.sqlbuilder.Progress;
import org.sqlbuilder.SQLProcessor;
import org.sqlbuilder.common.Progress;

public abstract class FnProcessor1 extends SQLProcessor
{
	protected final String fnHome;

	protected final String filename;

	public FnProcessor1(final String filename, final Properties props)
	{
		this.filename = filename;
		this.fnHome = props.getProperty("fnhome", System.getenv().get("FNHOME"));
	}

	@Override
	protected void run(final Connection connection) throws Exception
	{
		Progress.traceHeader("reading framenet file", this.filename);
		final File file = new File(this.fnHome + File.separatorChar + this.filename);
		processFrameNetFile(file.getCanonicalPath(), file.getName(), connection);
		Progress.trace(1);
		Progress.traceTailer("reading framenet file", this.filename, 1L);
	}

	@SuppressWarnings({ "SameReturnValue", "UnusedReturnValue" })
	protected abstract int processFrameNetFile(final String fileName, final String name, final Connection connection);
}
