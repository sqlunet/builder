package org.sqlbuilder.pb.collectors;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XmlDocument;
import org.sqlbuilder.pb.PbModule;
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

public class PbExportCollector extends PbCollector
{
	public PbExportCollector(final Properties conf)
	{
		super(conf);
	}

	@Override
	public void run()
	{
		final File folder = new File(this.propBankHome);
		final FilenameFilter filter = (dir, name) -> name.endsWith(".xml");
		final File[] fileArray = folder.listFiles(filter);
		if (fileArray == null)
		{
			return;
		}
		final List<File> files = Arrays.asList(fileArray);
		files.sort(Comparator.comparing(File::getName));
		int fileCount = 0;
		Progress.traceHeader("propbank", "reading files");
		for (final File file : files)
		{
			fileCount++;
			processPropBankFile(file.getAbsolutePath(), file.getName());

			Progress.trace(fileCount);
		}
		Progress.traceTailer(fileCount);
	}

	private void processPropBankFile(final String fileName, final String name)
	{
		final String head = name.split("\\.")[0];
		try
		{
			final PbDocument document = new PbDocument(fileName);
			processFrameset(document, XmlDocument.getXPath(document.getDocument(), "./frameset"), head);
		}
		catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e)
		{
			Logger.instance.logXmlException(PbModule.MODULE_ID, tag, fileName, e);
		}
	}

	protected void processFrameset(final PbDocument document, final Node start, final String head)
	{
		try
		{
			// rolesets
			PbDocument.makeRoleSets(head, start);

			// roles
			PbDocument.makeRoles(head, start);
		}
		catch (XPathExpressionException e)
		{
			Logger.instance.logXmlException(PbModule.MODULE_ID, tag, document.getFileName(), e);
		}
	}
}
