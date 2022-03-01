package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Checker;
import org.sqlbuilder.common.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

public class VnSyntaxXmlProcessor extends XmlProcessor
{
	private static final boolean LOG_ONLY = true;

	static final String SEP_CAT = "\n";
	static final String START_SELRESTR = " <";
	static final char END_SELRESTR = '>';
	static final String START_SYNRESTR = " {";
	static final char END_SYNRESTR = '}';

	static final String CATS = "ADJ|ADV|LEX|NP|PREP|VERB|VP";

	private static String PREPS = "about|after|against|among|as|as_if|as_though|at|before|between|by|concerning|for|from|in|in_between|into|like|of|off|on|onto|out_of|over|regarding|respecting|through|to|toward|towards|under|until|upon|with";

	private static String NP = "Affector|Agent|Asset|Attribute|Axis|Beneficiary|Cause|Causer|Circumstance|Co-Agent|Co-Patient|Co-Theme|Context|Destination|Duration|Eventuality|Experiencer|Extent|Goal|Initial_Location|Initial_State|Instrument|Location|Maleficiary|Manner|Material|Path|Patient|Pivot|Precondition|Predicate|Product|Recipient|Reflexive|Result|Source|Stimulus|Subeventuality|Theme|Time|Topic|Trajectory|Value";

	private static String VP = "Eventuality";

	private static String ADV = "Manner|Path|Trajectory";

	private static String LEX = "and|apart|as|at|away|down|like|of|out|together|up|'s|there|to|be|to be|\\[\\+be\\]|it|it\\[\\+be\\]";

	static final String SYNRESTR = "ac_ing|ac_to_inf|adv_loc|be_sc_ing|body_part|definite|for_comp|genitive|how_extract|np_ing|np_omit_ing|np_p_ing|np_ppart|np_to_be|np_to_inf|oc_bare_inf|oc_ing|oc_to_inf|plural|pos_ing|poss_ing|possing|quotation|refl|rs_to_inf|sc_ing|sc_to_inf|sentential|small_clause|tensed_that|that_comp|to_be|to_inf_rs|what_extract|vc_to_inf|wh_comp|wh_inf|what_inf|wheth_inf|wh_extract|wh_ing";

	static final String SELRESTR = "at|body_part|concrete|dest|dest_conf|dest_dir|dir|loc|location|path|region|refl|src|spatial|state|gol|src_conf";

	public VnSyntaxXmlProcessor()
	{
		//
	}

	@Override
	public String process(final String xml) throws ParserConfigurationException, SAXException, IOException
	{
		if (xml == null || xml.isEmpty())
		{
			return null;
		}
		final Element e = XmlProcessor.docFromString("<root>" + xml + "</root>");
		return VnSyntaxXmlProcessor.process(e);
	}

	private static String[] getAttrs(final String attrs)
	{
		boolean containsBar = attrs.contains("|");
		return Stream.of(attrs.split(containsBar ? " *\\| *" : "\\s+")) //
				.map(s -> s.replace(' ', '_')) //
				.toArray(String[]::new);
	}

	private static String process(final Element root) throws RuntimeException
	{
		root.normalize();

		final StringBuilder sb = new StringBuilder();

		// CAT
		final NodeList cats = root.getChildNodes();
		final int m = cats.getLength();
		for (int i = 0; i < m; i++)
		{
			final Node node = cats.item(i);
			if (!(node instanceof Element))
			{
				// TODO Checker.checkEmpty(node, "root", LOG_ONLY);
				continue;
			}
			final Element e = (Element) node;

			// name
			final String name = e.getNodeName();
			Checker.checkElementName(name, CATS, "SYNTAX " + name + " element found, expected " + CATS);
			sb.append(name);

			// attribute check
			Checker.checkAttributeName(e, "value", "SYNTAX " + name + " has no 'value' attr", LOG_ONLY);

			// 'value' attribute value
			String value = e.getAttribute("value").trim();
			if (!value.isEmpty())
			{
				switch (name)
				{
					case "PREP":
					{
						String[] values = getAttrs(value.trim());
						var values2 = Stream.of(values).map(s -> s.charAt(0) == '?' ? s.substring(1) : s).toArray(String[]::new); //
						Checker.checkAttributeValues(values2, PREPS, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						value = String.join(" | ", values);
						break;
					}
					case "NP":
					{
						value = value.trim();
						Checker.checkAttributeValue(value.charAt(0) == '?' ? value.substring(1) : value, NP, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						break;
					}
					case "LEX":
					{
						Checker.checkAttributeValue(value.trim(), LEX, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						break;
					}
					case "VERB":
					{
						System.out.println("VERB has 'value' attr: " + value);
						break;
					}
					case "ADV":
					{
						Checker.checkAttributeValue(value.trim(), ADV, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						break;
					}
					case "ADJ":
					{
						System.out.println("ADJ has 'value' attr: " + value);
						break;
					}
					case "VP":
					{
						Checker.checkAttributeValue(value.trim(), VP, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						break;
					}
					default:
					{
						System.out.println(name + " has 'value' attr: " + value);
						break;
					}
				}

				sb.append(' ');
				sb.append(value);
			}

			// SELRESTRS|SYNRESTRS
			final NodeList nodes2 = node.getChildNodes();
			for (int j = 0; j < nodes2.getLength(); j++)
			{
				final Node node2 = nodes2.item(j);
				if (!(node2 instanceof Element))
				{
					// TODO Checker.checkEmpty(node2, "SYNTAX found non element as child of " + name, LOG_ONLY);
					continue;
				}
				final Element e2 = (Element) node2;

				// name
				final String name2 = e2.getNodeName();
				Checker.checkElementName(name2, "SELRESTRS|SYNRESTRS", "SYNTAX " + "SELRESTRS|SYNRESTRS expected, found element: " + name2);
				// sb.append(" = ");
				// sb.append(name2);

				// attribute check
				Checker.checkAttributeName(e2, "logic", "SYNTAX " + name + " has no 'logic' attr", LOG_ONLY);

				// 'Value' attribute value
				String logic = e2.getAttribute("logic").trim();
				if (!logic.isEmpty())
				{
					Checker.checkAttributeValue(logic, "or|and", "SYNTAX " + name + " has unexpected 'logic' attr: " + logic, LOG_ONLY);
				}

				// SELRESTR|SYNRESTR
				final NodeList nodes3 = node2.getChildNodes();
				for (int k = 0; k < nodes3.getLength(); k++)
				{
					final Node node3 = nodes3.item(k);
					if (!(node3 instanceof Element))
					{
						//TODO Checker.checkEmpty(node3, "SYNTAX " + "found non element as child of " + name2, LOG_ONLY);
						continue;
					}
					final Element e3 = (Element) node3;

					// name
					final String name3 = e3.getNodeName();
					final boolean isSynRestr = name3.equals("SYNRESTR");
					final boolean isSelRestr = name3.equals("SELRESTR");

					Checker.checkElementName(name3, "SELRESTR|SYNRESTR", "SYNTAX " + "SELRESTR|SYNRESTR expected, found element: " + name3);
					// sb.append(" - ");
					// sb.append(name3);

					// attribute check
					Checker.checkAttributeName(e3, "Value|type", "SYNTAX " + name3 + " has no 'Value|type' attr", LOG_ONLY);

					// 'Value' attribute value
					final String value3 = e3.getAttribute("Value").trim();
					if (!value3.isEmpty())
					{
						Checker.checkAttributeValue(value3, "(\\+|\\-)", "SYNTAX " + name3 + " has unexpected 'Value' attr: " + value3, LOG_ONLY);
					}

					// 'type' attribute value
					final String type3 = e3.getAttribute("type").trim();
					if (!type3.isEmpty())
					{
						if (isSynRestr)
						{
							Checker.checkAttributeValue(type3, VnSyntaxXmlProcessor.SYNRESTR, "SYNTAX " + name3 + " has unexpected synrestr 'Value' attr: " + type3, LOG_ONLY);
						}
						if (isSelRestr)
						{
							Checker.checkAttributeValue(type3, VnSyntaxXmlProcessor.SELRESTR, "SYNTAX " + name3 + " has unexpected selrestr 'Value' attr: " + type3, LOG_ONLY);
						}
					}

					if (!value3.isEmpty() || !type3.isEmpty())
					{
						sb.append(isSynRestr ? VnSyntaxXmlProcessor.START_SYNRESTR : VnSyntaxXmlProcessor.START_SELRESTR);
						sb.append(value3);
						sb.append(type3);
						sb.append(isSynRestr ? VnSyntaxXmlProcessor.END_SYNRESTR : VnSyntaxXmlProcessor.END_SELRESTR);
					}

					if (logic.isEmpty())
					{
						logic = "and";
					}
					if (k < nodes3.getLength() - 1)
					{
						sb.append(' ');
						sb.append(logic);
					}
				}
			}
			if (i < m - 1)
			{
				sb.append(VnSyntaxXmlProcessor.SEP_CAT);
			}
		}
		return sb.toString();
	}
}
