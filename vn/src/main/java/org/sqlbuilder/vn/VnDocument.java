package org.sqlbuilder.vn;

import org.sqlbuilder.common.XPathUtils;
import org.sqlbuilder.vn.joins.VnFrameExampleMapping;
import org.sqlbuilder.vn.joins.VnPredicateMapping;
import org.sqlbuilder.vn.objects.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

public class VnDocument
{
	private Document document;

	public VnDocument(final String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		load(filePath);
	}

	private static DocumentBuilder makeDocumentBuilder() throws ParserConfigurationException
	{
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setCoalescing(true);
		factory.setIgnoringComments(true);
		factory.setNamespaceAware(false);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setValidating(false);
		factory.setExpandEntityReferences(false);
		//factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver((publicId, systemId) -> null);
		return builder;
	}

	private void load(final String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		final DocumentBuilder builder = VnDocument.makeDocumentBuilder();
		setDocument(builder.parse(filePath));
	}

	public void setDocument(final Document document)
	{
		this.document = document;
	}

	public Document getDocument()
	{
		return this.document;
	}

	// C L A S S

	public static Collection<VnItem> getItems(final Node start) throws XPathExpressionException
	{
		List<VnItem> result = null;
		final NodeList memberNodes = XPathUtils.getXPaths(start, "./MEMBERS/MEMBER");
		for (int i = 0; i < memberNodes.getLength(); i++)
		{
			final Element memberElement = (Element) memberNodes.item(i);
			final String lemmaAttribute = memberElement.getAttribute("name");
			final String wnAttribute = memberElement.getAttribute("wn");
			final String groupingAttribute = memberElement.getAttribute("grouping");
			VnItem item = VnItem.make(lemmaAttribute, wnAttribute, groupingAttribute);
			if (result == null)
			{
				result = new ArrayList<>();
			}
			result.add(item);
		}
		return result;
	}

	public static String getClassName(final Node start)
	{
		final Element classElement = (Element) start;
		return classElement.getAttribute("ID");
	}

	public static VnClassData getClassData(final Node start, final VnClass vnclass) throws TransformerException, XPathExpressionException, ParserConfigurationException, SAXException, IOException
	{
		final Set<VnRole> roles = VnDocument.makeRoles(start);
		final List<VnFrame> frames = VnDocument.makeFrames(start);
		return new VnClassData(vnclass, roles, frames);
	}

	// G R O U P I N G S

	public static Set<VnGrouping> makeGroupings(final Node start) throws XPathExpressionException
	{
		final Set<VnGrouping> result = new HashSet<>();
		final NodeList memberNodes = XPathUtils.getXPaths(start, "./MEMBERS/MEMBER");
		for (int i = 0; i < memberNodes.getLength(); i++)
		{
			final Element memberElement = (Element) memberNodes.item(i);
			final String groupingAttribute = memberElement.getAttribute("grouping");
			if (groupingAttribute == null || groupingAttribute.isEmpty())
			{
				continue;
			}
			final String[] groupingNames = groupingAttribute.split("\\s");
			for (final String groupingName : groupingNames)
			{
				final VnGrouping grouping = VnGrouping.make(groupingName);
				result.add(grouping);
			}
		}
		return result;
	}

	// R O L E

	public static Set<VnRole> makeRoles(final Node start) throws TransformerException, XPathExpressionException, IOException, SAXException, ParserConfigurationException
	{
		final Set<VnRole> result = new HashSet<>();
		final NodeList nodes = XPathUtils.getXPaths(start, "./THEMROLES/THEMROLE");
		if (nodes != null)
		{
			for (int i = 0; i < nodes.getLength(); i++)
			{
				final Element element = (Element) nodes.item(i);
				final String type = element.getAttribute("type");
				final NodeList restrsNodes = XPathUtils.getXPaths(element, "./SELRESTRS");
				final Element restrsElement = (Element) restrsNodes.item(0);
				final String selStrsXML = XPathUtils.getXML(restrsElement);
				// String logic = restrsElement.getAttribute("logic");
				result.add(VnRole.make(type, selStrsXML));
			}
		}
		return result;
	}

	public static Collection<VnRoleType> makeRoleTypes(final Node start) throws XPathExpressionException
	{
		final Collection<VnRoleType> result = new ArrayList<>();
		final NodeList nodes = XPathUtils.getXPaths(start, "./THEMROLES/THEMROLE");
		if (nodes != null)
		{
			for (int i = 0; i < nodes.getLength(); i++)
			{
				final Element roleElement = (Element) nodes.item(i);
				final String type = roleElement.getAttribute("type");
				result.add(VnRoleType.make(type));
			}
		}
		return result;
	}

	public static Collection<VnRestrType> makeSelRestrTypes(final Node start) throws XPathExpressionException
	{
		final Collection<VnRestrType> result = new ArrayList<>();
		final NodeList selNodes = XPathUtils.getXPaths(start, "//SELRESTR");
		if (selNodes != null)
		{
			for (int i = 0; i < selNodes.getLength(); i++)
			{
				final Element element = (Element) selNodes.item(i);
				final String restrValue = element.getAttribute("Value");
				final String restrType = element.getAttribute("type");
				final VnRestrType restr = VnRestrType.make(restrValue, restrType, false);
				result.add(restr);
			}
		}
		return result;
	}

	public static Collection<VnRestrType> makeSynRestrTypes(final Node start) throws XPathExpressionException
	{
		final Collection<VnRestrType> result = new ArrayList<>();
		final NodeList nodes = XPathUtils.getXPaths(start, "//SYNRESTR");
		if (nodes != null)
		{
			for (int i = 0; i < nodes.getLength(); i++)
			{
				final Element element = (Element) nodes.item(i);
				final String restrValue = element.getAttribute("Value");
				final String restrType = element.getAttribute("type");
				final VnRestrType restr = VnRestrType.make(restrValue, restrType, true);
				result.add(restr);
			}
		}
		return result;
	}

	public static Collection<VnRestrs> makeSelRestrs(final Node start) throws XPathExpressionException, TransformerException, ParserConfigurationException, SAXException, IOException
	{
		final Collection<VnRestrs> result = new ArrayList<>();
		final NodeList nodes = XPathUtils.getXPaths(start, "//SELRESTRS");
		if (nodes != null)
		{
			for (int i = 0; i < nodes.getLength(); i++)
			{
				final Element element = (Element) nodes.item(i);
				final String xML = XPathUtils.getXML(element);
				if (!xML.isEmpty() && !xML.equals("<SELRESTRS/>"))
				{
					final VnRestrs restrs = VnRestrs.make(xML, false);
					result.add(restrs);
				}
			}
		}
		return result;
	}

	public static Collection<VnRestrs> makeSynRestrs(final Node start) throws XPathExpressionException, TransformerException, ParserConfigurationException, SAXException, IOException
	{
		final Collection<VnRestrs> result = new ArrayList<>();
		final NodeList nodes = XPathUtils.getXPaths(start, "//SYNRESTRS");
		if (nodes != null)
		{
			for (int i = 0; i < nodes.getLength(); i++)
			{
				final Element element = (Element) nodes.item(i);
				final String xML = XPathUtils.getXML(element);
				if (!xML.isEmpty() && !xML.equals("<SYNRESTRS/>"))
				{
					final VnRestrs restrs = VnRestrs.make(xML, true);
					result.add(restrs);
				}
			}
		}
		return result;
	}

	// F R A M E

	public static List<VnFrame> makeFrames(final Node start) throws TransformerException, XPathExpressionException, IOException, SAXException, ParserConfigurationException
	{
		final List<VnFrame> result = new ArrayList<>();
		final NodeList frameNodes = XPathUtils.getXPaths(start, "./FRAMES/FRAME");
		for (int i = 0; i < frameNodes.getLength(); i++)
		{
			final Element frameElement = (Element) frameNodes.item(i);
			final Element description = (Element) XPathUtils.getXPath(frameElement, "./DESCRIPTION");
			final String descriptionPrimary = description.getAttribute("primary");
			final String descriptionSecondary = description.getAttribute("secondary");
			final String descriptionNumber = description.getAttribute("descriptionNumber");
			final String descriptionXTag = description.getAttribute("xtag");
			final String syntax = XPathUtils.getXML(XPathUtils.getXPath(frameElement, "./SYNTAX"));
			final String semantics = XPathUtils.getXML(XPathUtils.getXPath(frameElement, "./SEMANTICS"));

			final VnFrame frame = VnFrame.make(descriptionNumber, descriptionXTag, descriptionPrimary, descriptionSecondary, syntax, semantics);
			result.add(frame);
		}
		return result;
	}

	public static Collection<VnFrameName> makeFrameNames(final Node start) throws XPathExpressionException
	{
		final Collection<VnFrameName> result = new ArrayList<>();
		final NodeList nodes = XPathUtils.getXPaths(start, "./FRAMES/FRAME/DESCRIPTION");
		if (nodes != null)
		{
			for (int i = 0; i < nodes.getLength(); i++)
			{
				final Element element = (Element) nodes.item(i);
				final String name = element.getAttribute("primary");
				result.add(VnFrameName.make(name));
			}
		}
		return result;
	}

	public static Collection<VnFrameSubName> makeFrameSubNames(final Node start) throws XPathExpressionException
	{
		final Collection<VnFrameSubName> result = new ArrayList<>();
		final NodeList nodes = XPathUtils.getXPaths(start, "./FRAMES/FRAME/DESCRIPTION");
		if (nodes != null)
		{
			for (int i = 0; i < nodes.getLength(); i++)
			{
				final Element element = (Element) nodes.item(i);
				String subName = element.getAttribute("secondary");
				if (subName != null && !subName.isEmpty())
				{
					subName = subName.replaceAll("\\s+", " ");
					result.add(VnFrameSubName.make(subName));
				}
			}
		}
		return result;
	}

	public static Collection<VnFrameExample> makeFrameExamples(final Node start) throws XPathExpressionException
	{
		final Collection<VnFrameExample> result = new ArrayList<>();
		final List<String> examples = XPathUtils.getXPathTexts(start, "./FRAMES/FRAME/EXAMPLES/EXAMPLE");
		if (examples != null)
		{
			for (final String example : examples)
			{
				result.add(VnFrameExample.make(example));
			}
		}
		return result;
	}

	public static List<VnFrameExampleMapping> getFrameExampleMappings(final Node start) throws TransformerException, XPathExpressionException, IOException, SAXException, ParserConfigurationException
	{
		final List<VnFrameExampleMapping> result = new ArrayList<>();
		final NodeList frameNodes = XPathUtils.getXPaths(start, "./FRAMES/FRAME");
		for (int i = 0; i < frameNodes.getLength(); i++)
		{
			final Element frameElement = (Element) frameNodes.item(i);
			final Element description = (Element) XPathUtils.getXPath(frameElement, "./DESCRIPTION");
			final String descriptionPrimary = description.getAttribute("primary");
			final String descriptionSecondary = description.getAttribute("secondary");
			final String descriptionNumber = description.getAttribute("descriptionNumber");
			final String descriptionXTag = description.getAttribute("xtag");
			final String syntax = XPathUtils.getXML(XPathUtils.getXPath(frameElement, "./SYNTAX"));
			final String semantics = XPathUtils.getXML(XPathUtils.getXPath(frameElement, "./SEMANTICS"));

			final VnFrame frame = VnFrame.make(descriptionNumber, descriptionXTag, descriptionPrimary, descriptionSecondary, syntax, semantics);

			final List<String> examples = XPathUtils.getXPathTexts(frameElement, "./EXAMPLES/EXAMPLE");
			if (examples != null)
			{
				for (final String example : examples)
				{
					final VnFrameExample vnExample = VnFrameExample.make(example);
					result.add(new VnFrameExampleMapping(frame, vnExample));
				}
			}
		}
		return result;
	}

	public static Collection<VnSyntax> makeSyntaxes(final Node start) throws XPathExpressionException, TransformerException, ParserConfigurationException, SAXException, IOException
	{
		final Collection<VnSyntax> result = new ArrayList<>();
		final List<String> syntaxes = XPathUtils.getXML(XPathUtils.getXPaths(start, "./FRAMES/FRAME/SYNTAX"));
		// if (syntaxes != null)
		// {
		for (final String syntax : syntaxes)
		{
			result.add(VnSyntax.make(syntax));
		}
		// }
		return result;
	}

	public static Collection<VnSemantics> makeSemantics(final Node start) throws XPathExpressionException, TransformerException, ParserConfigurationException, SAXException, IOException
	{
		final Collection<VnSemantics> result = new ArrayList<>();
		final List<String> semanticss = XPathUtils.getXML(XPathUtils.getXPaths(start, "./FRAMES/FRAME/SEMANTICS"));
		// if (semanticss != null)
		// {
		for (final String semantics : semanticss)
		{
			result.add(VnSemantics.make(semantics));
		}
		// }
		return result;
	}

	public static Collection<VnPredicate> getPredicates(final Node start) throws XPathExpressionException
	{
		final Collection<VnPredicate> result = new ArrayList<>();
		final NodeList nodes = XPathUtils.getXPaths(start, "./FRAMES/FRAME/SEMANTICS/PRED");
		if (nodes != null)
		{
			for (int i = 0; i < nodes.getLength(); i++)
			{
				final Element element = (Element) nodes.item(i);
				final String predicate = element.getAttribute("value");
				result.add(new VnPredicate(predicate));
			}
		}
		return result;
	}

	public static List<VnPredicateMapping> getPredicateMappings(final Node start) throws TransformerException, XPathExpressionException, ParserConfigurationException, SAXException, IOException
	{
		final List<VnPredicateMapping> result = new ArrayList<>();
		final NodeList semanticsNodes = XPathUtils.getXPaths(start, "./FRAMES/FRAME/SEMANTICS");
		for (int i = 0; i < semanticsNodes.getLength(); i++)
		{
			final Node semanticNode = semanticsNodes.item(i);
			final String semanticsXml = XPathUtils.getXML(semanticNode);
			final VnSemantics semantics = VnSemantics.make(semanticsXml);

			final NodeList predNodes = XPathUtils.getXPaths(semanticNode, "./PRED");
			if (predNodes != null)
			{
				for (int j = 0; j < predNodes.getLength(); j++)
				{
					final Element predicateElement = (Element) predNodes.item(j);
					final VnPredicate predicate = new VnPredicate(predicateElement.getAttribute("value"));
					result.add(new VnPredicateMapping(semantics, predicate));
				}
			}
		}
		return result;
	}

	// URI

	public String getFileName()
	{
		final String uriString = getDocument().getDocumentURI();
		if (uriString != null)
		{
			try
			{
				final URI uri = new URI(uriString);
				final String path = uri.getPath();
				final File file = new File(path);
				return file.getName();
			}
			catch (URISyntaxException e)
			{
				//
			}
		}
		return null;
	}
}
