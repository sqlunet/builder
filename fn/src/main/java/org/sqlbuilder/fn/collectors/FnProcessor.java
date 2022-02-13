package org.sqlbuilder.fn.collectors;

import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public abstract class FnProcessor extends Processor
{
	protected final String fnHome;

	protected final String fnDir;

	protected String filename;

	protected int fileCount;

	public FnProcessor(final String subDir, final Properties props, final String tag)
	{
		super(tag);
		this.fnHome = props.getProperty("fn_home", System.getenv().get("FNHOME"));
		this.fnDir = subDir;
		this.fileCount = 0;
	}

	@Override
	public void run()
	{
		final String folderName = this.fnHome + File.separatorChar + this.fnDir;
		final File folder = new File(folderName);
		final FilenameFilter filter = (dir, filename2) -> filename2.endsWith(".xml");

		final File[] fileArray = folder.listFiles(filter);
		if (fileArray == null)
		{
			return;
		}
		final List<File> files = Arrays.asList(fileArray);
		files.sort(Comparator.comparing(File::getName));

		Progress.traceHeader("framenet files", this.fnDir);
		int fileCount = 0;
		for (final File file : files)
		{
			this.filename = file.getName();
			try
			{
				processFrameNetFile(file.getAbsolutePath(), file.getName());
				fileCount++;
			}
			catch (Exception e)
			{
				throw new RuntimeException("File:" + this.filename, e);
			}
			Progress.trace(fileCount);
		}
		Progress.traceTailer(fileCount);
	}

	@SuppressWarnings("SameReturnValue")
	protected abstract void processFrameNetFile(final String fileName, final String name);
}
