package org.sqlbuilder.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XmlDocument
{
	protected Document document;

	public XmlDocument(final String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		load(filePath);
	}

	private static DocumentBuilder makeDocumentBuilder() throws ParserConfigurationException
	{
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setCoalescing(true);
		factory.setIgnoringComments(true);
		factory.setNamespaceAware(false);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setValidating(false);
		factory.setExpandEntityReferences(false);
		//factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		
		return factory.newDocumentBuilder();
	}

	private void load(final String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		final DocumentBuilder builder = XmlDocument.makeDocumentBuilder();
		setDocument(builder.parse(filePath));
	}

	public void setDocument(final Document document)
	{
		this.document = document;
	}

	public Document getDocument()
	{
		return this.document;
	}

	public static NodeList getXPaths(final Node start, final String xpathExpr) throws XPathExpressionException
	{
		final XPath xpath = XPathFactory.newInstance().newXPath();
		return (NodeList) xpath.evaluate(xpathExpr, start, XPathConstants.NODESET);
	}

	public static Node getXPath(final Node start, final String xpathExpr) throws XPathExpressionException
	{
		final XPath xpath = XPathFactory.newInstance().newXPath();
		return (Node) xpath.evaluate(xpathExpr, start, XPathConstants.NODE);
	}

	public static List<String> getXPathTexts(final Node start, final String xpathExpr) throws XPathExpressionException
	{
		List<String> result = null;
		final NodeList nodes = XmlDocument.getXPaths(start, xpathExpr);
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

	public static String getXPathText(final Node start, final String xpathExpr) throws XPathExpressionException
	{
		final Node node = XPathUtils.getXPath(start, xpathExpr);
		if (node == null)
			return null;
		final Element element = (Element) node;
		return element.getTextContent().trim();
	}

	public String getFileName()
	{
		final String uriString = getDocument().getDocumentURI();
		if (uriString != null)
		{
			try
			{
				final URI uri = new URI(uriString);
				final String path = uri.getPath();
				final File file = new File(path);
				return file.getName();
			}
			catch (URISyntaxException localURISyntaxException)
			{
				//
			}
		}
		return null;
	}
}
