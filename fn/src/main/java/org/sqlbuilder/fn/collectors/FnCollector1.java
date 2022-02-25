package org.sqlbuilder.fn.collectors;

import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;

import java.io.File;
import java.util.Properties;

public abstract class FnCollector1 extends Processor
{
	protected final String fnHome;

	protected final String filename;

	public FnCollector1(final String filename, final Properties props, final String tag)
	{
		super(tag);
		this.filename = filename;
		this.fnHome = props.getProperty("fn_home", System.getenv().get("FNHOME"));
	}

	@Override
	public void run()
	{
		Progress.traceHeader("framenet file", this.filename);
		final File file = new File(this.fnHome + File.separatorChar + this.filename);
		processFrameNetFile(file.getAbsolutePath());
		Progress.trace(1);
		Progress.traceTailer(1);
	}

	@SuppressWarnings({ "SameReturnValue", "UnusedReturnValue" })
	protected abstract void processFrameNetFile(final String fileName);
}
