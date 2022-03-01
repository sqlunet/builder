package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
import org.sqlbuilder.vn.VnDocument;
import org.sqlbuilder.vn.VnModule;
import org.sqlbuilder.vn.objects.Word;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

public class VnUpdateCollector extends VnCollector
{
	public VnUpdateCollector(final Properties props)
	{
		super(props);
	}

	@Override
	public void run()
	{
		final File folder = new File(this.verbNetHome);
		final FilenameFilter filter = (dir, name) -> name.endsWith(".xml");

		final File[] fileArray = folder.listFiles(filter);
		if (fileArray == null)
		{
			return;
		}
		final List<File> files = Arrays.asList(fileArray);
		files.sort(Comparator.comparing(File::getName));

		// iterate

		int fileCount = 0;
		Progress.traceHeader("reading verbnet files", "");
		for (final File file : files)
		{
			fileCount++;
			processVerbNetFile(file.getAbsolutePath(), file.getName());
		}
		Progress.traceTailer(fileCount);
	}

	@Override
	protected void processVerbNetFile(final String fileName, final String name)
	{
		final String head = name.split("-")[0];
		try
		{
			final VnDocument document = new VnDocument(fileName);
			processVerbNetClass(XPathUtils.getXPath(document.getDocument(), "./VNCLASS"), head);
		}
		catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, tag, fileName, e);
			Progress.traceDone(e.toString());
		}
	}

	protected void processVerbNetClass(final Node start, final String head)
	{
		try
		{
			processMembers(start, head);

			// recurse
			final NodeList subclasses = XPathUtils.getXPaths(start, "./SUBCLASSES/VNSUBCLASS");
			for (int i = 0; i < subclasses.getLength(); i++)
			{
				final Node subNode = subclasses.item(i);
				processVerbNetClass(subNode, head);
			}
		}
		catch (XPathExpressionException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, tag, start.getOwnerDocument().getDocumentURI(), e);
		}
	}

	private static void processMembers(final Node start, final String head) throws XPathExpressionException
	{
		Word.make(head);
		VnDocument.makeResolvableMembers(start);
	}
}
