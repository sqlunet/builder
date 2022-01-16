package org.sqlbuilder.vn;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

public class Vn3Processor extends VnProcessor
{
	public Vn3Processor(final Properties props)
	{
		super(props,"vn3");
	}

	@Override
	protected void run()
	{
		Progress.traceHeader("verbnet pass3", null);
		super.run();
	}

	@Override
	protected void processVerbNetClass(final VnDocument document, final Node start, final String head)
	{
		try
		{
			// get frame example mappings
			final Collection<VnFrameExampleMapping> frameExampleMappings = VnDocument.getFrameExampleMappings(start);
			VnFrameExampleMapping.SET.addAll(frameExampleMappings);

			// get predicate mappings
			final Collection<VnPredicateMapping> predicateMappings = VnDocument.getPredicateMappings(start);
			VnPredicateMapping.SET.addAll(predicateMappings);

			// recurse
			final NodeList subclasses = XPathUtils.getXPaths(start, "./SUBCLASSES/VNSUBCLASS");
			for (int i = 0; i < subclasses.getLength(); i++)
			{
				final Node subNode = subclasses.item(i);
				processVerbNetClass(document, subNode, head);
			}
		}
		catch (XPathExpressionException | TransformerException | SAXException | ParserConfigurationException | IOException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, this.tag, "read-class", document.getFileName(), -1, null, "xml", e);
		}
	}
}
