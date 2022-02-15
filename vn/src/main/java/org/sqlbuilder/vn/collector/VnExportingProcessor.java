package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
import org.sqlbuilder.vn.Inherit;
import org.sqlbuilder.vn.VnDocument;
import org.sqlbuilder.vn.VnModule;
import org.sqlbuilder.vn.joins.*;
import org.sqlbuilder.vn.objects.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

public class VnExportingProcessor extends VnProcessor
{
	public VnExportingProcessor(final Properties props)
	{
		super(props);
	}

	@Override
	protected void processVerbNetClass(final Node start, final String head, final Collection<Role> inheritedRoles, final Collection<Frame> ignored)
	{
		try
		{
			final VnClass clazz = processClass(start);
			processItems(start);
			Collection<Role> inheritableRoles = processRoles(start, clazz, inheritedRoles);

			// recurse
			final NodeList subclasses = XPathUtils.getXPaths(start, "./SUBCLASSES/VNSUBCLASS");
			for (int i = 0; i < subclasses.getLength(); i++)
			{
				final Node subNode = subclasses.item(i);
				processVerbNetClass(subNode, head, inheritableRoles, ignored);
			}
		}
		catch (XPathExpressionException | TransformerException | ParserConfigurationException | SAXException | IOException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, this.tag, "read-class", head, -1, null, "xml", e);
		}
	}

	private static VnClass processClass(final Node start) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		return VnDocument.makeClass(start);
	}

	private static void processItems(final Node start) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		// get role types
		VnDocument.makeRoleTypes(start);

		// get roles
		VnDocument.makeRoles(start);
	}

	private static Collection<Role> processRoles(final Node start, final VnClass clazz, final Collection<Role> inheritedRoles) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		// roles
		Collection<Role> roles = VnDocument.makeRoles(start);
		if (inheritedRoles != null)
		{
			roles = Inherit.mergeRoles(roles, inheritedRoles);
		}

		// collect roles
		for (Role role : roles)
		{
			Class_Role.make(clazz, role);
		}

		// return data to be inherited by subclasses
		return roles;
	}
}
