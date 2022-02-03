package org.sqlbuilder.pb;

import org.sqlbuilder.pb.objects.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class SemlinkDocument extends PbDocument
{
	public SemlinkDocument(final String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		super(filePath);
	}

	public static Map<Role, PbVnRole> getMappings(final Node start) throws XPathExpressionException
	{
		final Map<Role, PbVnRole> map = new TreeMap<>();
		final NodeList predicateNodes = PbDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final Predicate predicate = Predicate.make(lemmaAttribute, lemmaAttribute);

			final NodeList argmapNodes = PbDocument.getXPaths(predicateElement, "./argmap");
			for (int j = 0; j < argmapNodes.getLength(); j++)
			{
				final Element argmapElement = (Element) argmapNodes.item(j);

				final String roleSetIdAttribute = argmapElement.getAttribute("pb-roleset");
				final RoleSet roleSet = RoleSet.make(predicate, roleSetIdAttribute, null, null);

				final String vnClassAttribute = argmapElement.getAttribute("vn-class");
				final PbVnClass vnClass = PbVnClass.make(predicate.getHead(), vnClassAttribute);

				final NodeList roleNodes = PbDocument.getXPaths(argmapElement, "./role");
				for (int k = 0; k < roleNodes.getLength(); k++)
				{
					final Element roleElement = (Element) roleNodes.item(k);

					final String argAttribute = roleElement.getAttribute("pb-arg");
					final String fAttribute = roleElement.getAttribute("pb-f");
					final String thetaAttribute = roleElement.getAttribute("vn-theta");
					final Role role = Role.make(roleSet, argAttribute, fAttribute, thetaAttribute, null);

					final PbVnRole vnRole = PbVnRole.make(vnClass, thetaAttribute);

					map.put(role, vnRole);
					// System.out.println(role + " -> " + vnRole);
				}
			}
		}
		return map;
	}
}
