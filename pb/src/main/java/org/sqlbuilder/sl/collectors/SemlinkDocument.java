package org.sqlbuilder.sl.collectors;

import org.sqlbuilder.XmlDocument;
import org.sqlbuilder.pb.foreign.VnClass;
import org.sqlbuilder.pb.foreign.VnRole;
import org.sqlbuilder.pb.joins.PbRole_VnRole;
import org.sqlbuilder.pb.objects.Predicate;
import org.sqlbuilder.pb.objects.Role;
import org.sqlbuilder.pb.objects.RoleSet;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class SemlinkDocument extends XmlDocument
{
	public SemlinkDocument(final String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		super(filePath);
	}

	/*
	<predicate lemma="abandon">
		<argmap pb-roleset="abandon.01" vn-class="51.2">
			<role pb-arg="0" vn-theta="Theme" />
		</argmap>
	</predicate>
	*/

	public static void makeMappings(final Node start) throws XPathExpressionException
	{
		final NodeList predicateNodes = XmlDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final Predicate predicate = Predicate.make(lemmaAttribute, lemmaAttribute);

			final NodeList argmapNodes = XmlDocument.getXPaths(predicateElement, "./argmap");
			for (int j = 0; j < argmapNodes.getLength(); j++)
			{
				final Element argmapElement = (Element) argmapNodes.item(j);

				// propbank roleset
				final String roleSetIdAttribute = argmapElement.getAttribute("pb-roleset");
				final RoleSet roleSet = RoleSet.make(predicate, roleSetIdAttribute, null);

				// verbnet class
				final String vnClassAttribute = argmapElement.getAttribute("vn-class");
				final VnClass vnClass = VnClass.make(predicate.getHead(), vnClassAttribute);

				// roles
				final NodeList roleNodes = XmlDocument.getXPaths(argmapElement, "./role");
				for (int k = 0; k < roleNodes.getLength(); k++)
				{
					// role
					final Element roleElement = (Element) roleNodes.item(k);

					// pb attributes (arg,f)
					final String argAttribute = roleElement.getAttribute("pb-arg");
					final String fAttribute = roleElement.getAttribute("pb-f");
					// vn attributes (theta)
					final String thetaAttribute = roleElement.getAttribute("vn-theta");

					// propbank role
					final Role role = Role.make(roleSet, argAttribute, fAttribute, thetaAttribute, null);

					// vn role
					final VnRole vnRole = VnRole.make(vnClass, thetaAttribute);

					PbRole_VnRole.make(role, vnRole);
				}
			}
		}
	}
}
