package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.XPathUtils;
import org.sqlbuilder.vn.Inherit;
import org.sqlbuilder.vn.VnDocument;
import org.sqlbuilder.vn.VnModule;
import org.sqlbuilder.vn.objects.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	protected void processVerbNetClass(final Node start, final String head, final Collection<RestrainedRole> inheritedRestrainedRoles, final Collection<Frame> ignored)
	{
		try
		{
			final VnClass clazz = processClass(start);
			processItems(start);
			processMembers(start, head);
			Collection<RestrainedRole> inheritableRestrainedRoles = processRoles(start, clazz, inheritedRestrainedRoles);

			// recurse
			final NodeList subclasses = XPathUtils.getXPaths(start, "./SUBCLASSES/VNSUBCLASS");
			for (int i = 0; i < subclasses.getLength(); i++)
			{
				final Node subNode = subclasses.item(i);
				processVerbNetClass(subNode, head, inheritableRestrainedRoles, ignored);
			}
		}
		catch (XPathExpressionException | TransformerException | ParserConfigurationException | SAXException | IOException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, tag, start.getNodeName(), e);
		}
	}

	private static VnClass processClass(final Node start) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		return VnDocument.makeClass(start);
	}

	private static void processMembers(final Node start, final String head) throws XPathExpressionException
	{
		// members
		Collection<Member> members = VnDocument.makeMembers(start);
		members.add(Member.make(head, null, null));

		// member
		for (final Member member : members)
		{
			// word
			Word.make(member.lemma);
		}
	}

	private static void processItems(final Node start) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		// get role types
		VnDocument.makeRoleTypes(start);

		// get roles
		VnDocument.makeRoles(start);
	}

	private static Collection<RestrainedRole> processRoles(final Node start, final VnClass clazz, final Collection<RestrainedRole> inheritedRestrainedRoles) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		// roles
		Collection<RestrainedRole> restrainedRoles = VnDocument.makeRoles(start);
		if (inheritedRestrainedRoles != null)
		{
			restrainedRoles = Inherit.mergeRoles(restrainedRoles, inheritedRestrainedRoles);
		}

		// collect roles
		for (RestrainedRole restrainedRole : restrainedRoles)
		{
			Role.make(clazz, restrainedRole);
		}

		// return data to be inherited by subclasses
		return restrainedRoles;
	}
}
