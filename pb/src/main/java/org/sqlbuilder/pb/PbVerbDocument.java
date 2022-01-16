package org.sqlbuilder.pb;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class PbVerbDocument extends PbDocument
{
	public PbVerbDocument(final String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		super(filePath);
	}

	public static Collection<PbPredicate> getPredicates(final String head, final Node start) throws XPathExpressionException
	{
		List<PbPredicate> result = null;
		final NodeList predicateNodes = PbDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final PbPredicate predicate = PbPredicate.make(head, lemmaAttribute);
			if (result == null)
			{
				result = new ArrayList<>();
			}
			result.add(predicate);
		}
		return result;
	}

	public static Collection<PbLexItem> getAliasPredicates(final Node start) throws XPathExpressionException
	{
		List<PbLexItem> result = null;
		final NodeList aliasNodes = PbDocument.getXPaths(start, ".//alias");
		for (int i = 0; i < aliasNodes.getLength(); i++)
		{
			final Element aliasElement = (Element) aliasNodes.item(i);
			final String lemma = aliasElement.getTextContent(); // $NON-NLS-1$
			final PbLexItem predicate = PbLexItem.make(lemma);
			if (result == null)
			{
				result = new ArrayList<>();
			}
			result.add(predicate);
		}
		return result;
	}

	public static Collection<PbRoleSet> getRoleSets(final String head, final Node start) throws XPathExpressionException
	{
		List<PbRoleSet> result = null;
		final NodeList predicateNodes = PbDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final PbPredicate predicate = PbPredicate.make(head, lemmaAttribute);

			final NodeList roleSetNodes = PbDocument.getXPaths(predicateElement, "./roleset");
			for (int j = 0; j < roleSetNodes.getLength(); j++)
			{
				// roleset data
				final Element roleSetElement = (Element) roleSetNodes.item(j);
				final String roleSetIdAttribute = roleSetElement.getAttribute("id");
				final String roleSetNameAttribute = roleSetElement.getAttribute("name");

				// roleset aliases
				final List<PbAlias> aliases = new ArrayList<>();
				final NodeList aliasRoleNodes = PbDocument.getXPaths(roleSetElement, "./aliases/alias");
				if (aliasRoleNodes != null && aliasRoleNodes.getLength() > 0)
				{
					for (int l = 0; l < aliasRoleNodes.getLength(); l++)
					{
						final Element aliasElement = (Element) aliasRoleNodes.item(l);
						final String pos = aliasElement.getAttribute("pos").trim();
						final String lemma = aliasElement.getTextContent().trim();
						final PbWord pbWord = new PbWord(lemma);

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
								final PbAlias alias = new PbAlias(PbAlias.Db.VERBNET, clazz, pos, lemma, null, null);
								aliases.add(alias);
							}
						}

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
								final PbAlias alias = new PbAlias(PbAlias.Db.FRAMENET, frame, pos, lemma, null, null);
								aliases.add(alias);
							}
						}
					}
				}

				final PbRoleSet roleSet = PbRoleSet.make(predicate, roleSetIdAttribute, roleSetNameAttribute, aliases);
				if (result == null)
				{
					result = new ArrayList<>();
				}
				result.add(roleSet);
			}
		}
		return result;
	}

	public static Collection<PbRole> getRoles(final String head, final Node start) throws XPathExpressionException
	{
		List<PbRole> result = null;
		final NodeList predicateNodes = PbDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final PbPredicate predicate = PbPredicate.make(head, lemmaAttribute);

			final NodeList roleSetNodes = PbDocument.getXPaths(predicateElement, "./roleset");
			for (int j = 0; j < roleSetNodes.getLength(); j++)
			{
				// roleset data
				final Element roleSetElement = (Element) roleSetNodes.item(j);
				final String roleSetIdAttribute = roleSetElement.getAttribute("id");
				final String nameAttribute = roleSetElement.getAttribute("name");

				// roleset
				final PbRoleSet roleSet = PbRoleSet.make(predicate, roleSetIdAttribute, nameAttribute, null);

				final NodeList roleNodes = PbDocument.getXPaths(roleSetElement, "./roles/role");
				for (int k = 0; k < roleNodes.getLength(); k++)
				{
					final Element roleElement = (Element) roleNodes.item(k);

					// attributes
					final String nAttribute = roleElement.getAttribute("n");
					final String fAttribute = roleElement.getAttribute("f");
					final String descriptorAttribute = roleElement.getAttribute("descr");

					// theta
					String theta = null;
					final NodeList vnRoleNodes = PbDocument.getXPaths(roleElement, "./vnrole");
					if (vnRoleNodes != null && vnRoleNodes.getLength() > 0)
					{
						final Element vnRoleElement = (Element) vnRoleNodes.item(0);
						theta = vnRoleElement.getAttribute("vntheta");
					}

					// role
					final PbRole role = PbRole.make(roleSet, nAttribute, fAttribute, theta, descriptorAttribute);
					if (result == null)
					{
						result = new ArrayList<>();
					}
					result.add(role);

					// role-vnrole maps
					PbVerbDocument.makeVnRoleMaps(head, role, roleElement);
				}
			}
		}
		return result;
	}

	private static void makeVnRoleMaps(final String head, final PbRole role, final Element roleElement) throws XPathExpressionException
	{
		final NodeList vnRoleNodes = PbDocument.getXPaths(roleElement, "./vnrole");
		for (int l = 0; l < vnRoleNodes.getLength(); l++)
		{
			final Element vnRoleElement = (Element) vnRoleNodes.item(l);
			final String classAttribute = vnRoleElement.getAttribute("vncls");
			final PbVnClass vnClass = new PbVnClass(head, classAttribute);

			final String thetaAttribute = vnRoleElement.getAttribute("vntheta");
			final PbVnRole vnRole = new PbVnRole(vnClass, thetaAttribute);
			if (PbVnRoleMapping.map == null)
			{
				PbVnRoleMapping.map = new TreeMap<>();
			}
			final List<PbVnRole> vnRoles = PbVnRoleMapping.map.computeIfAbsent(role, k -> new ArrayList<>());
			vnRoles.add(vnRole);
		}
	}

	public static Collection<PbExample> getExamples(final String head, final Node start) throws XPathExpressionException
	{
		List<PbExample> result = null;
		final NodeList predicateNodes = PbDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final PbPredicate predicate = PbPredicate.make(head, lemmaAttribute);

			final NodeList roleSetNodes = PbDocument.getXPaths(predicateElement, "./roleset");
			for (int j = 0; j < roleSetNodes.getLength(); j++)
			{
				final Element roleSetElement = (Element) roleSetNodes.item(j);
				final String roleSetIdAttribute = roleSetElement.getAttribute("id");
				final String nameAttribute = roleSetElement.getAttribute("name");
				final PbRoleSet roleSet = PbRoleSet.make(predicate, roleSetIdAttribute, nameAttribute, null);

				final NodeList exampleNodes = PbDocument.getXPaths(roleSetElement, "./example");
				for (int k = 0; k < exampleNodes.getLength(); k++)
				{
					final Element exampleElement = (Element) exampleNodes.item(k);

					final String exampleName = exampleElement.getAttribute("name");
					final String exampleText = PbDocument.getXPathText(exampleElement, "./text");

					String aspect = null;
					String form = null;
					String person = null;
					String tense = null;
					String voice = null;
					PbExample example;
					final Node inflectionNode = PbDocument.getXPath(exampleElement, "./inflection");
					if (inflectionNode != null)
					{
						final Element inflectionElement = (Element) inflectionNode;
						aspect = inflectionElement.getAttribute("aspect");
						form = inflectionElement.getAttribute("form");
						person = inflectionElement.getAttribute("person");
						tense = inflectionElement.getAttribute("tense");
						voice = inflectionElement.getAttribute("voice");
					}
					example = PbExample.make(roleSet, exampleName, exampleText, aspect, form, person, tense, voice);

					// relations
					final NodeList relNodes = PbDocument.getXPaths(exampleElement, "./rel");
					for (int l = 0; l < relNodes.getLength(); l++)
					{
						final Element relElement = (Element) relNodes.item(l);

						final String f = relElement.getAttribute("f");
						final String relText = relElement.getTextContent().trim();
						final PbRel rel = PbRel.make(example, relText, f);
						example.rels.add(rel);
					}

					// arguments
					final NodeList argNodes = PbDocument.getXPaths(exampleElement, "./arg");
					for (int m = 0; m < argNodes.getLength(); m++)
					{
						final Element argElement = (Element) argNodes.item(m);

						final String f = argElement.getAttribute("f");
						final String n = argElement.getAttribute("n");
						final String argText = argElement.getTextContent().trim();
						final PbArg arg = PbArg.make(example, argText, n, f);
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

	public static Collection<PbArg> getExampleArgs(final String head, final Node start) throws XPathExpressionException
	{
		List<PbArg> result = null;
		final NodeList predicateNodes = PbDocument.getXPaths(start, "./predicate");
		for (int i = 0; i < predicateNodes.getLength(); i++)
		{
			final Element predicateElement = (Element) predicateNodes.item(i);
			final String lemmaAttribute = predicateElement.getAttribute("lemma");
			final PbPredicate predicate = PbPredicate.make(head, lemmaAttribute);

			final NodeList roleSetNodes = PbDocument.getXPaths(predicateElement, "./roleset");
			for (int j = 0; j < roleSetNodes.getLength(); j++)
			{
				final Element roleSetElement = (Element) roleSetNodes.item(j);
				final String roleSetIdAttribute = roleSetElement.getAttribute("id");
				final String nameAttribute = roleSetElement.getAttribute("name");
				final PbRoleSet roleSet = PbRoleSet.make(predicate, roleSetIdAttribute, nameAttribute, null);

				final NodeList exampleNodes = PbDocument.getXPaths(roleSetElement, "./example");
				for (int k = 0; k < exampleNodes.getLength(); k++)
				{
					final Element exampleElement = (Element) exampleNodes.item(k);

					final String exampleName = exampleElement.getAttribute("name");
					final String exampleText = PbDocument.getXPathText(exampleElement, "./text");

					String aspect = null;
					String form = null;
					String person = null;
					String tense = null;
					String voice = null;
					PbExample example;
					final Node inflectionNode = PbDocument.getXPath(exampleElement, "./inflection");
					if (inflectionNode != null)
					{
						final Element inflectionElement = (Element) inflectionNode;
						aspect = inflectionElement.getAttribute("aspect");
						form = inflectionElement.getAttribute("form");
						person = inflectionElement.getAttribute("person");
						tense = inflectionElement.getAttribute("tense");
						voice = inflectionElement.getAttribute("voice");
					}
					example = PbExample.make(roleSet, exampleName, exampleText, aspect, form, person, tense, voice);

					// args
					final NodeList argNodes = PbDocument.getXPaths(exampleElement, "./arg");
					for (int l = 0; l < argNodes.getLength(); l++)
					{
						final Element argElement = (Element) argNodes.item(l);

						final String f = argElement.getAttribute("f");
						final String n = argElement.getAttribute("n");
						final String argText = argElement.getTextContent().trim();
						final PbArg arg = PbArg.make(example, argText, n, f);
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
