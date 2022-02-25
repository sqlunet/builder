package org.sqlbuilder.common;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Checker
{
	private Checker()
	{
	}

	public static int errors = 0;

	public static void checkEmpty(final Node node, final String message) throws RuntimeException
	{
		if (!node.getNodeValue().matches(" *"))
		{
			++Checker.errors;
			throw new RuntimeException("empty node " + node + "| " + (message == null ? "" : message));
		}
	}

	public static void checkSubElements(final Element e, final String m, final String message) throws RuntimeException
	{
		final NodeList nodes = e.getChildNodes();
		// if (nodes.getLength() > 1)
		// {++error;throw new RuntimeException(e.getNodeName() + " has multiple nodes " + (message == null ? "" : message));
		//
		for (int j = 0; j < nodes.getLength(); j++)
		{
			final Node node = nodes.item(j);
			if (node instanceof Element)
			{
				if (m == null)
				{
					++Checker.errors;
					throw new RuntimeException(e.getNodeName() + " has element node |" + node.getNodeName() + "| " + (message == null ? "" : message));
				}
				Checker.checkElementName(node.getNodeName(), m, message);
			}
		}
	}

	public static void checkElementName(final String name, final String m, final String message) throws RuntimeException
	{
		if (!name.matches(m))
		{
			++Checker.errors;
			throw new RuntimeException("element |" + name + "| " + (message == null ? "" : message));
		}
	}

	public static void checkAttributeName(final Element e, final String m, final String message) throws RuntimeException
	{
		final NamedNodeMap attrs = e.getAttributes();
		if (m != null)
		{
			for (int i = 0; i < attrs.getLength(); i++)
			{
				final Node attr = attrs.item(i);
				final String name = attr.getNodeName();
				if (!name.matches(m))
				{
					++Checker.errors;
					throw new RuntimeException("attribute in " + e.getNodeName() + " |" + name + "| " + (message == null ? "" : message));
				}
			}
		}
		else
		{
			if (attrs.getLength() != 0)
			{
				final StringBuilder sb = new StringBuilder();
				for (int i = 0; i < attrs.getLength(); i++)
				{
					if (i > 0)
					{
						sb.append(' ');
					}
					final Node attr = attrs.item(i);
					sb.append(attr.getNodeName());
				}
				++Checker.errors;
				throw new RuntimeException("attribute |" + sb + "| " + (message == null ? "" : message));
			}
		}
	}

	public static void checkAttributeValue(final String value, final String m, final String message) throws RuntimeException
	{
		if (value == null || value.isEmpty())
		{
			return;
		}
		final String[] items = value.split("\\s+");
		for (final String item : items)
		{
			if (!item.matches(m))
			{
				++Checker.errors;
				throw new RuntimeException("attribute value |" + item + "| " + (message == null ? "" : message));
			}
		}
	}
}
