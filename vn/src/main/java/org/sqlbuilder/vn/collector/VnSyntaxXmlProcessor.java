package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.XmlChecker;
import org.sqlbuilder.common.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

public class VnSyntaxXmlProcessor extends XmlProcessor
{
	private static final boolean LOG_ONLY = false;
	private static final boolean TARGET_34 = true;

	static final String SEP_CAT = "\n";
	static final String START_SELRESTR = " <";
	static final char END_SELRESTR = '>';
	static final String START_SYNRESTR = " {";
	static final char END_SYNRESTR = '}';

	static final String CATS = "ADJ|ADV|LEX|NP|PREP|VERB|VP";

	private static String NP_REGEX33 = "Affector|Agent|Asset|Attribute|Axis|Beneficiary|Causer|Co-Agent|Context|Co-Patient|Co-Theme|Destination|Duration|Experiencer|Extent|Goal|Initial_Location|Initial_State|Instrument|Location|Manner|Material|Path|Patient|Pivot|Precondition|Predicate|Product|Recipient|Reflexive|Result|Source|Stimulus|Theme|Time|Topic|Trajectory|Value";
	private static String NP_REGEX34 = "Affector|Agent|Asset|Attribute|Axis|Beneficiary|Causer|Circumstance|Co-Agent|Co-Patient|Co-Theme|Destination|Duration|Eventuality|Experiencer|Extent|Goal|Initial_Location|Initial_State|Instrument|Location|Maleficiary|Manner|Material|Path|Patient|Pivot|Precondition|Product|Recipient|Reflexive|Result|Result|Source|Stimulus|Subeventuality|Theme|Topic|Trajectory|Value";

	private static String PREP_REGEX33 = "about|after|against|among|as|as_if|as_though|at|before|between|by|concerning|for|from|in|in_between|into|like|of|off|on|onto|out_of|over|regarding|respecting|through|to|towards|under|until|upon|with";
	private static String PREP_REGEX34 = "about|after|against|among|as|as_if|as_though|at|before|between|by|concerning|for|from|in|in_between|into|like|of|off|on|onto|out_of|over|regarding|respecting|through|to|toward|towards|under|upon|with";

	private static String LEX_REGEX33 = "and|apart|as|at|away|be|down|it|like|of|out|'s|there|to|to be|together|up|\\[\\+be\\]|it\\[\\+be\\]";
	private static String LEX_REGEX34 = "and|apart|as|at|be|down|it|like|of|out|'s|there|to|to be|together|up|\\[\\+be\\]|it\\[\\+be\\]";

	private static String ADV_REGEX33 = "Trajectory";
	private static String ADV_REGEX34 = "Manner|Path|Trajectory";

	private static String VP_REGEX33 = ".*";
	private static String VP_REGEX34 = "Eventuality";

	static final String SELRESTR = "at|body_part|concrete|dest|dest_conf|dest_dir|dir|gol|loc|location|path|refl|region|spatial|src|src_conf|state";
	static final String SYNRESTR = "ac_ing|ac_to_inf|adv_loc|be_sc_ing|definite|for_comp|genitive|how_extract|np_ing|np_omit_ing|np_p_ing|np_ppart|np_to_inf|oc_bare_inf|oc_ing|oc_to_inf|plural|poss_ing|quotation|refl|rs_to_inf|sc_ing|sc_to_inf|sentential|small_clause|tensed_that|that_comp|to_be|what_extract|what_inf|wh_comp|wheth_inf|wh_extract|wh_inf|wh_ing";

	static final Pattern NP_PATTERN = Pattern.compile(TARGET_34 ? NP_REGEX34 : NP_REGEX33, Pattern.CASE_INSENSITIVE);
	static final Pattern PREP_PATTERN = Pattern.compile(TARGET_34 ? PREP_REGEX34 : PREP_REGEX33, Pattern.CASE_INSENSITIVE);
	static final Pattern LEX_PATTERN = Pattern.compile(TARGET_34 ? LEX_REGEX34 : LEX_REGEX33, Pattern.CASE_INSENSITIVE);
	static final Pattern ADV_PATTERN = Pattern.compile(TARGET_34 ? ADV_REGEX34 : ADV_REGEX33, Pattern.CASE_INSENSITIVE);
	static final Pattern VP_PATTERN = Pattern.compile(TARGET_34 ? VP_REGEX34 : VP_REGEX33, Pattern.CASE_INSENSITIVE);

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
			XmlChecker.checkElementName(name, CATS, "SYNTAX " + name + " element found, expected " + CATS);
			sb.append(name);

			// attribute check
			XmlChecker.checkAttributeName(e, "value", "SYNTAX " + name + " has no 'value' attr", LOG_ONLY);

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
						XmlChecker.checkAttributeValues(values2, PREP_PATTERN, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						value = String.join(" | ", values);
						//System.out.println("@PREP " + value);
						break;
					}
					case "NP":
					{
						value = value.trim();
						XmlChecker.checkAttributeValue(value.charAt(0) == '?' ? value.substring(1) : value, NP_PATTERN, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						//System.out.println("@NP " + value);
						break;
					}
					case "LEX":
					{
						XmlChecker.checkAttributeValue(value.trim(), LEX_PATTERN, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						//System.out.println("@LEX " + value);
						break;
					}
					case "ADV":
					{
						XmlChecker.checkAttributeValue(value.trim(), ADV_PATTERN, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						//System.out.println("@ADV " + value);
						break;
					}
					case "VP":
					{
						XmlChecker.checkAttributeValue(value.trim(), VP_PATTERN, "SYNTAX " + name + " has unexpected 'value' attr: " + value, LOG_ONLY);
						//System.out.println("@VP " + value);
						break;
					}
					case "VERB":
					case "ADJ":
					default:
					{
						System.out.println(name + " has 'value' attr: " + value);
						//System.out.println("@" + name + " " + value);
						break;
					}
				}

				// collect value
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
				XmlChecker.checkElementName(name2, "SELRESTRS|SYNRESTRS", "SYNTAX " + "SELRESTRS|SYNRESTRS expected, found element: " + name2);
				// sb.append(" = ");
				// sb.append(name2);

				// attribute check
				XmlChecker.checkAttributeName(e2, "logic", "SYNTAX " + name + " has no 'logic' attr", LOG_ONLY);

				// 'Value' attribute value
				String logic = e2.getAttribute("logic").trim();
				if (!logic.isEmpty())
				{
					XmlChecker.checkAttributeValue(logic, "or|and", "SYNTAX " + name + " has unexpected 'logic' attr: " + logic, LOG_ONLY);
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

					XmlChecker.checkElementName(name3, "SELRESTR|SYNRESTR", "SYNTAX " + "SELRESTR|SYNRESTR expected, found element: " + name3);
					// sb.append(" - ");
					// sb.append(name3);

					// attribute check
					XmlChecker.checkAttributeName(e3, "Value|type", "SYNTAX " + name3 + " has no 'Value|type' attr", LOG_ONLY);

					// 'Value' attribute value
					final String value3 = e3.getAttribute("Value").trim();
					if (!value3.isEmpty())
					{
						XmlChecker.checkAttributeValue(value3, "(\\+|\\-)", "SYNTAX " + name3 + " has unexpected 'Value' attr: " + value3, LOG_ONLY);
					}

					// 'type' attribute value
					final String type3 = e3.getAttribute("type").trim();
					if (!type3.isEmpty())
					{
						if (isSynRestr)
						{
							XmlChecker.checkAttributeValue(type3, VnSyntaxXmlProcessor.SYNRESTR, "SYNTAX " + name3 + " has unexpected synrestr 'Value' attr: " + type3, LOG_ONLY);
							//System.out.println("@SYN " + type3);
						}
						if (isSelRestr)
						{
							XmlChecker.checkAttributeValue(type3, VnSyntaxXmlProcessor.SELRESTR, "SYNTAX " + name3 + " has unexpected selrestr 'Value' attr: " + type3, LOG_ONLY);
							//System.out.println("@SEL " + type3);
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
