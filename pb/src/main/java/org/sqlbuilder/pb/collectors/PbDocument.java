package org.sqlbuilder.pb.collectors;

import org.sqlbuilder.XmlDocument;
import org.sqlbuilder.pb.foreign.Alias;
import org.sqlbuilder.pb.foreign.VnClass;
import org.sqlbuilder.pb.foreign.VnRole;
import org.sqlbuilder.pb.joins.PbRole_VnRole;
import org.sqlbuilder.pb.objects.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class PbDocument extends XmlDocument
{
	public PbDocument(final String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		super(filePath);
	}

	public static Collection<Predicate> getPredicates(final String head, final Node start) throws XPathExpressionException
	{
		List<Predicate> result = null;
		final NodeList predicateNodes = XmlDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final Predicate predicate = Predicate.make(head, lemmaAttribute);
			if (result == null)
			{
				result = new ArrayList<>();
			}
			result.add(predicate);
		}
		return result;
	}

	public static Collection<LexItem> getAliasPredicates(final Node start) throws XPathExpressionException
	{
		List<LexItem> result = null;
		final NodeList aliasNodes = XmlDocument.getXPaths(start, ".//alias");
		for (int i = 0; i < aliasNodes.getLength(); i++)
		{
			final Element aliasElement = (Element) aliasNodes.item(i);
			final String lemma = aliasElement.getTextContent(); // $NON-NLS-1$
			final LexItem predicate = LexItem.make(lemma);
			if (result == null)
			{
				result = new ArrayList<>();
			}
			result.add(predicate);
		}
		return result;
	}

	public static Collection<RoleSet> makeRoleSets(final String head, final Node start) throws XPathExpressionException
	{
		List<RoleSet> result = null;
		final NodeList predicateNodes = XmlDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final Predicate predicate = Predicate.make(head, lemmaAttribute);

			// predicate as roleset member
			PbWord pbword = PbWord.make(predicate.getLemma());

			final NodeList roleSetNodes = XmlDocument.getXPaths(predicateElement, "./roleset");
			for (int j = 0; j < roleSetNodes.getLength(); j++)
			{
				// roleset data
				final Element roleSetElement = (Element) roleSetNodes.item(j);
				final String roleSetIdAttribute = roleSetElement.getAttribute("id");
				final String roleSetNameAttribute = roleSetElement.getAttribute("name");

				// roleset
				final RoleSet roleSet = RoleSet.make(predicate, roleSetIdAttribute, roleSetNameAttribute);
				if (result == null)
				{
					result = new ArrayList<>();
				}
				result.add(roleSet);

				// roleset member
				Member.make(roleSet, pbword);

				// roleset aliases
				final List<Alias> aliases = roleSet.getAliases();
				final NodeList aliasRoleNodes = XmlDocument.getXPaths(roleSetElement, "./aliases/alias");
				if (aliasRoleNodes != null && aliasRoleNodes.getLength() > 0)
				{
					for (int l = 0; l < aliasRoleNodes.getLength(); l++)
					{
						final Element aliasElement = (Element) aliasRoleNodes.item(l);
						final String pos = aliasElement.getAttribute("pos").trim();
						final String lemma = aliasElement.getTextContent().trim();

						// alias word
						PbWord pbword2 = PbWord.make(lemma);

						// alias word as roleset member
						Member.make(roleSet, pbword2);

						// v e r b n e t
						final String verbNet = aliasElement.getAttribute("verbnet").trim();
						if (!verbNet.isEmpty())
						{
							final String[] classes = verbNet.split("[\\s,]");
							for (String clazz : classes)
							{
								clazz = clazz.trim();
								if (clazz.isEmpty() || "-".equals(clazz))
								{
									continue;
								}
								final Alias alias = Alias.make(Alias.Db.VERBNET, clazz, pos, lemma, roleSet, pbword);
								aliases.add(alias);
							}
						}

						// f r a m e n e t
						final String frameNet = aliasElement.getAttribute("framenet").trim();
						if (!frameNet.isEmpty())
						{
							final String[] frames = frameNet.split("[\\s,]");
							for (String frame : frames)
							{
								frame = frame.trim();
								if (frame.isEmpty() || "-".equals(frame))
								{
									continue;
								}
								final Alias alias = Alias.make(Alias.Db.FRAMENET, frame, pos, lemma, roleSet, pbword);
								aliases.add(alias);
							}
						}
					}
				}
			}
		}
		return result;
	}

	public static Collection<Role> makeRoles(final String head, final Node start) throws XPathExpressionException
	{
		List<Role> result = null;
		final NodeList predicateNodes = XmlDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final Predicate predicate = Predicate.make(head, lemmaAttribute);

			final NodeList roleSetNodes = XmlDocument.getXPaths(predicateElement, "./roleset");
			for (int j = 0; j < roleSetNodes.getLength(); j++)
			{
				// roleset data
				final Element roleSetElement = (Element) roleSetNodes.item(j);
				final String roleSetIdAttribute = roleSetElement.getAttribute("id");
				final String nameAttribute = roleSetElement.getAttribute("name");

				// roleset
				final RoleSet roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute);

				final NodeList roleNodes = XmlDocument.getXPaths(roleSetElement, "./roles/role");
				for (int k = 0; k < roleNodes.getLength(); k++)
				{
					final Element roleElement = (Element) roleNodes.item(k);

					// attributes
					final String nAttribute = roleElement.getAttribute("n");
					final String fAttribute = roleElement.getAttribute("f");
					final String descriptorAttribute = roleElement.getAttribute("descr");

					// theta
					String theta = null;
					final NodeList vnRoleNodes = XmlDocument.getXPaths(roleElement, "./vnrole");
					if (vnRoleNodes != null && vnRoleNodes.getLength() > 0)
					{
						final Element vnRoleElement = (Element) vnRoleNodes.item(0);
						theta = vnRoleElement.getAttribute("vntheta");
					}

					// role
					final Role role = Role.make(roleSet, nAttribute, fAttribute, theta, descriptorAttribute);
					if (result == null)
					{
						result = new ArrayList<>();
					}
					result.add(role);

					// role-vnrole maps
					PbDocument.makeVnRoleMaps(head, role, roleElement);
				}
			}
		}
		return result;
	}

	private static void makeVnRoleMaps(final String head, final Role role, final Element roleElement) throws XPathExpressionException
	{
		final NodeList vnRoleNodes = XmlDocument.getXPaths(roleElement, "./vnrole");
		for (int l = 0; l < vnRoleNodes.getLength(); l++)
		{
			final Element vnRoleElement = (Element) vnRoleNodes.item(l);
			final String vnClassAttribute = vnRoleElement.getAttribute("vncls");
			final VnClass vnClass = VnClass.make(head, vnClassAttribute);
			final String thetaAttribute = vnRoleElement.getAttribute("vntheta");

			// verbnet role
			final VnRole vnRole = VnRole.make(vnClass, thetaAttribute);

			// propbank role -> verbnet roles
			PbRole_VnRole.make(role, vnRole);
		}
	}

	public static Collection<Example> makeExamples(final String head, final Node start) throws XPathExpressionException
	{
		List<Example> result = null;
		final NodeList predicateNodes = XmlDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final Predicate predicate = Predicate.make(head, lemmaAttribute);

			final NodeList roleSetNodes = XmlDocument.getXPaths(predicateElement, "./roleset");
			for (int j = 0; j < roleSetNodes.getLength(); j++)
			{
				final Element roleSetElement = (Element) roleSetNodes.item(j);
				final String roleSetIdAttribute = roleSetElement.getAttribute("id");
				final String nameAttribute = roleSetElement.getAttribute("name");
				final RoleSet roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute);

				final NodeList exampleNodes = XmlDocument.getXPaths(roleSetElement, "./example");
				for (int k = 0; k < exampleNodes.getLength(); k++)
				{
					final Element exampleElement = (Element) exampleNodes.item(k);

					final String exampleName = exampleElement.getAttribute("name");
					final String exampleText = XmlDocument.getXPathText(exampleElement, "./text");

					String aspect = null;
					String form = null;
					String person = null;
					String tense = null;
					String voice = null;
					Example example;
					final Node inflectionNode = XmlDocument.getXPath(exampleElement, "./inflection");
					if (inflectionNode != null)
					{
						final Element inflectionElement = (Element) inflectionNode;
						aspect = inflectionElement.getAttribute("aspect");
						form = inflectionElement.getAttribute("form");
						person = inflectionElement.getAttribute("person");
						tense = inflectionElement.getAttribute("tense");
						voice = inflectionElement.getAttribute("voice");
					}
					example = Example.make(roleSet, exampleName, exampleText, aspect, form, person, tense, voice);

					// relations
					final NodeList relNodes = XmlDocument.getXPaths(exampleElement, "./rel");
					for (int l = 0; l < relNodes.getLength(); l++)
					{
						final Element relElement = (Element) relNodes.item(l);

						final String f = relElement.getAttribute("f");
						final String relText = relElement.getTextContent().trim();
						final Func func = Func.make(f);
						final Rel rel = Rel.make(example, relText, func);
						example.rels.add(rel);
					}

					// arguments
					final NodeList argNodes = XmlDocument.getXPaths(exampleElement, "./arg");
					for (int m = 0; m < argNodes.getLength(); m++)
					{
						final Element argElement = (Element) argNodes.item(m);

						final String f = argElement.getAttribute("f");
						final String n = argElement.getAttribute("n");
						final String argText = argElement.getTextContent().trim();
						final Arg arg = Arg.make(example, argText, n, f);
						example.args.add(arg);
					}

					if (result == null)
					{
						result = new ArrayList<>();
					}
					result.add(example);
				}
			}
		}
		return result;
	}

	public static Collection<Arg> makeExampleArgs(final String head, final Node start) throws XPathExpressionException
	{
		List<Arg> result = null;
		final NodeList predicateNodes = XmlDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final Predicate predicate = Predicate.make(head, lemmaAttribute);

			final NodeList roleSetNodes = XmlDocument.getXPaths(predicateElement, "./roleset");
			for (int j = 0; j < roleSetNodes.getLength(); j++)
			{
				final Element roleSetElement = (Element) roleSetNodes.item(j);
				final String roleSetIdAttribute = roleSetElement.getAttribute("id");
				final String nameAttribute = roleSetElement.getAttribute("name");
				final RoleSet roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute);

				final NodeList exampleNodes = XmlDocument.getXPaths(roleSetElement, "./example");
				for (int k = 0; k < exampleNodes.getLength(); k++)
				{
					final Element exampleElement = (Element) exampleNodes.item(k);

					final String exampleName = exampleElement.getAttribute("name");
					final String exampleText = XmlDocument.getXPathText(exampleElement, "./text");

					String aspect = null;
					String form = null;
					String person = null;
					String tense = null;
					String voice = null;
					Example example;
					final Node inflectionNode = XmlDocument.getXPath(exampleElement, "./inflection");
					if (inflectionNode != null)
					{
						final Element inflectionElement = (Element) inflectionNode;
						aspect = inflectionElement.getAttribute("aspect");
						form = inflectionElement.getAttribute("form");
						person = inflectionElement.getAttribute("person");
						tense = inflectionElement.getAttribute("tense");
						voice = inflectionElement.getAttribute("voice");
					}
					example = Example.make(roleSet, exampleName, exampleText, aspect, form, person, tense, voice);

					// args
					final NodeList argNodes = XmlDocument.getXPaths(exampleElement, "./arg");
					for (int l = 0; l < argNodes.getLength(); l++)
					{
						final Element argElement = (Element) argNodes.item(l);

						final String f = argElement.getAttribute("f");
						final String n = argElement.getAttribute("n");
						final String argText = argElement.getTextContent().trim();
						final Arg arg = Arg.make(example, argText, n, f);
						if (result == null)
						{
							result = new ArrayList<>();
						}
						result.add(arg);
					}
				}
			}
		}
		return result;
	}
}
