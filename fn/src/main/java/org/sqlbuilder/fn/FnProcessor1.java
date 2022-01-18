package org.sqlbuilder.fn;

import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;

import java.io.File;
import java.util.Properties;

public abstract class FnProcessor1 extends Processor
{
	protected final String fnHome;

	protected final String filename;

	public FnProcessor1(final String filename, final Properties props, final String tag)
	{
		super(tag);
		this.filename = filename;
		this.fnHome = props.getProperty("fnhome", System.getenv().get("FNHOME"));
	}

	@Override
	protected void run()
	{
		Progress.traceHeader("reading framenet file", this.filename);
		final File file = new File(this.fnHome + File.separatorChar + this.filename);
		processFrameNetFile(file.getAbsolutePath(), file.getName());
		Progress.trace(1);
		Progress.traceTailer("reading framenet file ", this.filename);
	}

	@SuppressWarnings({ "SameReturnValue", "UnusedReturnValue" })
	protected abstract int processFrameNetFile(final String fileName, final String name);
}
