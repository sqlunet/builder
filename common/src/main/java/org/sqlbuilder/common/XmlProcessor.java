package org.sqlbuilder.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public abstract class XmlProcessor
{
	protected XmlProcessor()
	{
		//
	}

	public abstract String process(String xml) throws ParserConfigurationException, SAXException, IOException;

	static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	public static Element docFromString(final String xml) throws IOException, SAXException, ParserConfigurationException
	{
		try
		{
			final DocumentBuilder builder = XmlProcessor.factory.newDocumentBuilder();
			return builder.parse(new ByteArrayInputStream(xml.getBytes())).getDocumentElement();
		}
		catch (SAXException | ParserConfigurationException | IOException e)
		{
			System.err.println(xml);
			throw e;
		}
	}
}
