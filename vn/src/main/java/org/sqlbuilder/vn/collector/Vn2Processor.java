package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
import org.sqlbuilder.vn.VnDocument;
import org.sqlbuilder.vn.VnModule;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

public class Vn2Processor extends VnProcessor
{
	public Vn2Processor(final Properties props)
	{
		super(props, "vn2");
	}

	@Override
	public void run()
	{
		Progress.traceHeader("verbnet pass2", null);
		super.run();
	}

	@Override
	protected void processVerbNetClass(final VnDocument document, final Node start, final String head)
	{
		try
		{
			// get role
			VnDocument.makeRoles(start);

			// get frames
			VnDocument.makeFrames(start);

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
