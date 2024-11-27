package org.sqlbuilder.sl.collectors;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XmlDocument;
import org.sqlbuilder.sl.SlModule;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class SemlinkUpdatingProcessor extends SemlinkProcessor
{
	public SemlinkUpdatingProcessor(final Properties conf)
	{
		super(conf);
	}

	@Override
	public void run()
	{
		try
		{
			final SemlinkDocument document = new SemlinkDocument(this.semlinkFile);
			processSemlinks(XmlDocument.getXPath(document.document, "./pbvn-typemap"));
		}
		catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e)
		{
			Logger.instance.logXmlException(SlModule.MODULE_ID, tag, semlinkFile, e);
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private static void processSemlinks(final Node start) throws XPathExpressionException
	{
		Progress.traceHeader("semlink", "reading file");
		SemlinkDocument.makeMappings(start);
		Progress.traceTailer(1);
	}
}
