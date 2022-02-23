package org.sqlbuilder.common;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

public class XmlTextUtils
{
	public static List<String> getXPathTexts(final Node start, final String xpathExpr) throws XPathExpressionException
	{
		List<String> result = null;
		final NodeList nodes = XPathUtils.getXPaths(start, xpathExpr);
		for (int i = 0; i < nodes.getLength(); i++)
		{
			if (result == null)
			{
				result = new ArrayList<>();
			}
			final Element element = (Element) nodes.item(i);
			String text = element.getTextContent().trim();
			text = text.replaceFirst("^\"*", "");
			text = text.replaceFirst("\"*$", "");
			text = text.replaceFirst("[.;]*$", "");
			result.add(text);
		}
		return result;
	}

	// X M L A S T E X T

	public static List<String> getXML(final NodeList nodes) throws TransformerException
	{
		final List<String> result = new ArrayList<>();
		for (int i = 0; i < nodes.getLength(); i++)
		{
			result.add(getXML(nodes.item(i)));
		}
		return result;
	}

	public static String getXML(final Node node) throws TransformerException
	{
		// output stream
		final StringWriter outStream = new StringWriter();
		final Result resultStream = new StreamResult(outStream);

		// source
		final Source source = new DOMSource(node);

		// output
		final TransformerFactory factory = TransformerFactory.newInstance();
		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		final Transformer transformer = factory.newTransformer();
		transformer.transform(source, resultStream);
		String result = outStream.toString();
		final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		if (result.startsWith(header))
		{
			result = result.substring(header.length());
		}
		return result;
	}
}
