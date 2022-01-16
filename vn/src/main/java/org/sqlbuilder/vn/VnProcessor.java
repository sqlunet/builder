package org.sqlbuilder.vn;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public abstract class VnProcessor extends Processor
{
	protected final String verbNetHome;

	protected int fileCount;

	public VnProcessor(final Properties props, final String tag)
	{
		super(tag);
		this.verbNetHome = props.getProperty("vnhome", System.getenv().get("VNHOME"));
		this.fileCount = 0;
	}

	@Override
	protected void run()
	{
		final File folder = new File(this.verbNetHome);
		final FilenameFilter filter = (dir, name) -> name.endsWith(".xml");

		final File[] fileArray = folder.listFiles(filter);
		if (fileArray == null)
			return;
		final List<File> files = Arrays.asList(fileArray);
		files.sort(Comparator.comparing(File::getName));

		// iterate

		this.fileCount = 0;
		Progress.traceHeader("reading verbnet files", "");
		for (final File file : files)
		{
			this.fileCount++;
			processVerbNetFile(file.getAbsolutePath(), file.getName());

			// trace
			Progress.trace(this.fileCount);
		}
		Progress.traceTailer("reading verbnet files", this.fileCount);
	}

	@SuppressWarnings("UnusedReturnValue")
	private void processVerbNetFile(final String fileName, final String name)
	{
		final String head = name.split("-")[0];
		if (Logger.verbose)
		{
			Progress.traceHeader("verbnet " + head, "");
		}
		long count = 0;
		try
		{
			final VnDocument document = new VnDocument(fileName);
			processVerbNetClass(document, XPathUtils.getXPath(document.getDocument(), "./VNCLASS"), head);
		}
		catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, this.tag, "xml-document", fileName, -1, null, "document=[" + fileName + "]", e);
		}

		if (Logger.verbose)
		{
			Progress.traceTailer("verbnet " + head, count);
		}
	}

	protected abstract void processVerbNetClass(final VnDocument document, final Node start, final String head);
}
