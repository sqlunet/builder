package org.sqlbuilder.common;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker
{
	private Checker()
	{
	}

	public static int errors = 0;

	public static void checkEmpty(final Node node, final String context, final boolean logOnly) throws RuntimeException
	{
		if (!node.getNodeValue().matches(" *"))
		{
			++Checker.errors;
			if (logOnly)
			{
				System.err.println("empty node <" + node + "> " + (context == null ? "" : context));
			}
			else
			{
				throw new RuntimeException("empty node <" + node + "> " + (context == null ? "" : context));
			}
		}
	}

	public static void checkSubElements(final Element e, final String regex, final String context, final boolean logOnly) throws RuntimeException
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
				if (regex == null)
				{
					++Checker.errors;
					if (logOnly)
					{
						System.err.println(e.getNodeName() + " has element node <" + node.getNodeName() + "> " + (context == null ? "" : context));
					}
					else
					{
						throw new RuntimeException(e.getNodeName() + " has element node <" + node.getNodeName() + "> " + (context == null ? "" : context));
					}
				}
				Checker.checkElementName(node.getNodeName(), regex, context);
			}
		}
	}

	public static void checkElementName(final String name, final String regex, final String context) throws RuntimeException
	{
		if (!name.matches(regex))
		{
			++Checker.errors;
			throw new RuntimeException("element |" + name + "| " + (context == null ? "" : context));
		}
	}

	public static boolean checkAttributeName(final Element e, final String regex, final String context, boolean logOnly) throws RuntimeException
	{
		final NamedNodeMap attrs = e.getAttributes();
		if (regex != null)
		{
			for (int i = 0; i < attrs.getLength(); i++)
			{
				final Node attr = attrs.item(i);
				final String name = attr.getNodeName();
				if (!name.matches(regex))
				{
					++Checker.errors;
					String message = "Illegal attribute in " + e.getNodeName() + " <" + name + "> in " + (context == null ? "" : context);
					if (!logOnly)
					{
						throw new RuntimeException(message);
					}
					else
					{
						System.err.println(message);
						return false;
					}
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
				String message = "Illegal attribute in " + e.getNodeName() + " <" + sb + "> in " + (context == null ? "" : context);
				if (!logOnly)
				{
					throw new RuntimeException(message);
				}
				else
				{
					System.err.println(message);
					return false;
				}
			}
		}
		return true;
	}

	public static boolean checkAttributeValue(final String value, final String regex, final String context, boolean logOnly) throws RuntimeException
	{
		return checkAttributeValue(value, "\\s+", regex, context, logOnly);
	}

	public static boolean checkAttributeValue(final String value, final String delim, final String regex, final String context, boolean logOnly) throws RuntimeException
	{
		if (value == null || value.isEmpty())
		{
			return true;
		}
		final String[] items = value.trim().split(delim);
		return checkAttributeValues(items, regex, context, logOnly);
	}

	public static boolean checkAttributeValues(final String[] values, final String regex, final String context, boolean logOnly) throws RuntimeException
	{
		if (values == null || values.length == 0)
		{
			return true;
		}

		final Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		for (final String value : values)
		{
			Matcher m= p.matcher(value);
			if (!m.matches())
			{
				++Checker.errors;
				String message = "Illegal attribute value <" + value + "> in " + (context == null ? "" : context);
				if (!logOnly)
				{
					throw new RuntimeException(message);
				}
				else
				{
					System.err.println(message);
					return false;
				}
			}
		}
		return true;
	}
}
