package org.sqlbuilder.sl.collectors;

import org.sqlbuilder.common.XmlDocument;
import org.sqlbuilder.sl.foreign.PbRole;
import org.sqlbuilder.sl.foreign.VnAlias;
import org.sqlbuilder.sl.foreign.VnRole;
import org.sqlbuilder.sl.foreign.VnRoleAlias;
import org.sqlbuilder.sl.objects.Predicate;
import org.sqlbuilder.sl.objects.Theta;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

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
			//noinspection ResultOfMethodCallIgnored
			Predicate.make(lemmaAttribute, lemmaAttribute);

			final NodeList argmapNodes = XmlDocument.getXPaths(predicateElement, "./argmap");
			for (int j = 0; j < argmapNodes.getLength(); j++)
			{
				final Element argmapElement = (Element) argmapNodes.item(j);

				// propbank roleset
				final String roleSetAttribute = argmapElement.getAttribute("pb-roleset");

				// verbnet class
				final String vnClassAttribute = argmapElement.getAttribute("vn-class");

				// map
				VnAlias.make(roleSetAttribute, vnClassAttribute);

				// roles
				final NodeList roleNodes = XmlDocument.getXPaths(argmapElement, "./role");
				for (int k = 0; k < roleNodes.getLength(); k++)
				{
					// role element
					final Element roleElement = (Element) roleNodes.item(k);

					// pb attributes (arg)
					final String argAttribute = roleElement.getAttribute("pb-arg");

					// vn attributes (theta)
					final String thetaAttribute = roleElement.getAttribute("vn-theta");

					// propbank role
					final PbRole pbRole = PbRole.make(roleSetAttribute, argAttribute);

					// vn role
					final VnRole vnRole = VnRole.make(vnClassAttribute, Theta.make(thetaAttribute));

					VnRoleAlias.make(pbRole, vnRole);
				}
			}
		}
	}
}
