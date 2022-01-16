package org.sqlbuilder.fn;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.sqlbuilder.Progress;
import org.sqlbuilder.SQLProcessor;

public abstract class FnProcessor extends SQLProcessor
{
	protected final String fnHome;

	protected final String fnDir;

	protected String filename;

	protected int fileCount;

	public FnProcessor(final String subDir, final Properties props)
	{
		this.fnHome = props.getProperty("fnhome", System.getenv().get("FNHOME"));
		this.fnDir = subDir;
		this.fileCount = 0;
	}

	@Override
	protected void run(final Connection connection) throws Exception
	{
		final String folderName = this.fnHome + File.separatorChar + this.fnDir;
		final File folder = new File(folderName);
		final FilenameFilter filter = (dir, filename2) -> filename2.endsWith(".xml");

		final File[] fileArray = folder.listFiles(filter);
		if (fileArray == null)
			return;
		final List<File> files = Arrays.asList(fileArray);
		files.sort(Comparator.comparing(File::getName));

		Progress.traceHeader("reading framenet files", this.fnDir);
		this.fileCount = 0;
		for (final File file : files)
		{
			this.filename = file.getName();
			try
			{
				this.fileCount += processFrameNetFile(file.getCanonicalPath(), file.getName(), connection);
			}
			catch (Exception e)
			{
				throw new RuntimeException("File:" + this.filename);
			}
			Progress.trace(this.fileCount);
		}
		Progress.traceTailer("reading framenet files", this.fnDir, this.fileCount);
	}

	@SuppressWarnings("SameReturnValue")
	protected abstract int processFrameNetFile(final String fileName, final String name, final Connection connection);
}
