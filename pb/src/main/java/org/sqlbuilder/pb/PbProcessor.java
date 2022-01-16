package org.sqlbuilder.pb;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public abstract class PbProcessor extends Processor
{
	protected final Properties conf;

	protected final String propBankHome;

	protected int fileCount;

	public PbProcessor(final Properties conf, final String tag)
	{
		super(tag);
		this.conf = conf;
		this.propBankHome = conf.getProperty("pbhome", System.getenv().get("PBHOME"));
		this.fileCount = 0;
	}

	@Override
	protected void run()
	{
		final File folder = new File(this.propBankHome);
		final FilenameFilter filter = (dir, name) -> name.endsWith(".xml");
		final File[] fileArray = folder.listFiles(filter);
		if (fileArray == null)
			return;
		final List<File> files = Arrays.asList(fileArray);
		files.sort(Comparator.comparing(File::getName));
		this.fileCount = 0;
		Progress.traceHeader("reading propbank files", "");
		for (final File file : files)
		{
			this.fileCount += 1;
			processPropBankFile(file.getAbsolutePath(), file.getName());

			Progress.trace(this.fileCount);
		}
		Progress.traceTailer("reading propbank files", this.fileCount);
	}

	@SuppressWarnings("UnusedReturnValue")
	private int processPropBankFile(final String fileName, final String name)
	{
		final String head = name.split("\\.")[0];
		if (Logger.verbose)
		{
			Progress.traceHeader("propbank " + head, "");
		}
		int count = 0;
		try
		{
			final PbVerbDocument document = new PbVerbDocument(fileName);
			count = processFrameset(document, PbDocument.getXPath(document.getDocument(), "./frameset"), head);
		}
		catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e)
		{
			Logger.instance.logXmlException("pb", this.tag, "xml-document", fileName, -1, null, "document=[" + fileName + "]", e);
		}
		if (Logger.verbose)
		{
			Progress.traceTailer("propbank " + head, count);
		}
		return count;
	}

	protected abstract int processFrameset(PbVerbDocument paramPbVerbDocument, Node paramNode, String paramString);

	protected static <T> void insertSet(final Set<T> set, final String table)
	{
	}

	protected static <T> void insertSet(final Set<T> set, final Properties props, final String table)
	{
	}

	protected static <T> void insertMap(final Map<T, Integer> map, final String table)
	{
	}

	protected static <T> void insertMap(final Map<T, Integer> map, final Properties props, final String table)
	{
	}
}
