package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.XmlChecker;
import org.sqlbuilder.common.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

public class VnRestrsXmlProcessor extends XmlProcessor
{
	private static final boolean LOG_ONLY = false;
	private static final boolean TARGET_34 = true;

	static final char START_SELRESTR = '<';
	static final char END_SELRESTR = '>';
	static final char START_SYNRESTR = '{';
	static final char END_SYNRESTR = '}';

	static final String RESTRS = "SELRESTRS|SYNRESTRS";

	static final String RESTRS2 = "SELRESTR|SYNRESTR|SELRESTRS";

	static final String SELRESTR_REGEX33 = "abstract|animal|animate|biotic|body_part|comestible|communication|concrete|currency|dest|dest_conf|dest_dir|dir|elongated|eventive|force|garment|human|int_control|loc|location|machine|nonrigid|organization|path|plural|pointy|refl|region|solid|sound|spatial|src|state|substance|time|vehicle";
	static final String SELRESTR_REGEX34 = "abstract|animal|animate|at|biotic|body_part|comestible|communication|concrete|currency|dest|dest_conf|dest_dir|dir|elongated|eventive|force|garment|gol|human|int_control|loc|location|machine|nonrigid|organization|path|plural|pointy|question|refl|region|solid|sound|spatial|src|src_conf|state|substance|vehicle|vehicle_part";

	static final String SYNRESTR_REGEX33 = "ac_ing|ac_to_inf|adv_loc|be_sc_ing|definite|for_comp|genitive|how_extract|np_ing|np_omit_ing|np_p_ing|np_ppart|np_to_inf|oc_bare_inf|oc_ing|oc_to_inf|plural|poss_ing|quotation|refl|rs_to_inf|sc_ing|sc_to_inf|sentential|small_clause|tensed_that|that_comp|to_be|what_extract|what_inf|wh_comp|wheth_inf|wh_extract|wh_inf|wh_ing";
	static final String SYNRESTR_REGEX34 = "ac_ing|ac_to_inf|adv_loc|be_sc_ing|definite|for_comp|genitive|how_extract|np_ing|np_omit_ing|np_p_ing|np_ppart|np_to_inf|oc_bare_inf|oc_ing|oc_to_inf|plural|poss_ing|quotation|refl|rs_to_inf|sc_ing|sc_to_inf|sentential|small_clause|tensed_that|that_comp|to_be|what_extract|what_inf|wh_comp|wheth_inf|wh_extract|wh_inf|wh_ing";

	static final Pattern SELRESTR_PATTERN = Pattern.compile(TARGET_34 ? SELRESTR_REGEX34 : SELRESTR_REGEX33, Pattern.CASE_INSENSITIVE);
	static final Pattern SYNRESTR_PATTERN = Pattern.compile(TARGET_34 ? SYNRESTR_REGEX34 : SYNRESTR_REGEX33, Pattern.CASE_INSENSITIVE);

	public VnRestrsXmlProcessor()
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
		return VnRestrsXmlProcessor.process(e);
	}

	private static String process(final Element root) throws RuntimeException
	{
		root.normalize();

		final StringBuilder sb = new StringBuilder();

		// SELRESTRS|SYNRESTRS
		final NodeList nodes2 = root.getChildNodes();
		for (int j = 0; j < nodes2.getLength(); j++)
		{
			final Node node2 = nodes2.item(j);
			if (!(node2 instanceof Element))
			{
				XmlChecker.checkEmpty(node2, "RESTR: non-empty non-element at root", LOG_ONLY);
				continue;
			}
			final Element e2 = (Element) node2;
			final String restrs = VnRestrsXmlProcessor.process2(e2);
			sb.append(restrs);
		}
		// if (i < m - 1)
		// sb.append(VnRestrsXmlProcessor.SEP_CAT);

		return sb.toString();
	}

	private static String process2(final Element e2) throws RuntimeException
	{
		e2.normalize();

		final StringBuilder sb = new StringBuilder();

		// SELRESTRS|SYNRESTRS

		// name
		final String name2 = e2.getNodeName();
		XmlChecker.checkElementName(name2, RESTRS, "RESTR: " + name2 + " found, expected: " + RESTRS);
		// sb.append(" = ");
		// sb.append(name2);

		// attribute check
		XmlChecker.checkAttributeName(e2, "logic", "RESTR: " + name2 + " has no 'logic' attribute", LOG_ONLY);

		// 'Value' attribute value
		String logic = e2.getAttribute("logic").trim();
		if (!logic.isEmpty())
		{
			XmlChecker.checkAttributeValue(logic, "or|and", "RESTR: " + name2 + " has 'logic' attribute value outside or|and", LOG_ONLY);
		}

		// SELRESTR|SYNRESTR
		final NodeList nodes3 = e2.getChildNodes();
		for (int k = 0; k < nodes3.getLength(); k++)
		{
			final Node node3 = nodes3.item(k);
			if (!(node3 instanceof Element))
			{
				//Checker.checkEmpty(node3, "RESTR: " + name2 + " has non-empty non-element " + node3, LOG_ONLY);
				continue;
			}
			final Element e3 = (Element) node3;

			// name
			final String name3 = e3.getNodeName();
			final boolean isSynRestr = name3.equals("SYNRESTR");
			final boolean isSelRestr = name3.equals("SELRESTR");

			XmlChecker.checkElementName(name3, RESTRS2, "RESTR: " + name2 + " has unexpected element " + name3 + ", expected: " + RESTRS2);
			// sb.append(" - ");
			// sb.append(name3);

			if ("SELRESTRS".equals(name3))
			{
				final String sub = VnRestrsXmlProcessor.process2(e3);
				sb.append("( ");
				sb.append(sub);
				sb.append(" )");
			}
			else
			{
				// attribute check
				XmlChecker.checkAttributeName(e3, "Value|type", "RESTR: " + name3 + " has no 'Value'|'type' attribute", LOG_ONLY);

				// 'Value' attribute value
				final String value3 = e3.getAttribute("Value").trim();
				if (!value3.isEmpty())
				{
					XmlChecker.checkAttributeValue(value3, "(\\+|\\-)", "RESTR: " + name3 + " has 'value' not in (+/-)", LOG_ONLY);
				}

				// 'type' attribute value
				final String type3 = e3.getAttribute("type").trim();
				if (!type3.isEmpty())
				{
					if (isSynRestr)
					{
						XmlChecker.checkAttributeValue(type3, SYNRESTR_PATTERN, "SYNRESTR: " + name3 + " has no 'type' in " + name3 + ", expected: " + SYNRESTR_PATTERN.pattern(), LOG_ONLY);
						//System.out.println("@SYN " + type3);
					}
					if (isSelRestr)
					{
						XmlChecker.checkAttributeValue(type3, SELRESTR_PATTERN, "SELRESTR: " + name3 + " has no 'type' in " + name3 + ", expected: " + SELRESTR_PATTERN.pattern(), LOG_ONLY);
						//System.out.println("@SEL " + type3);
					}
				}

				if (!value3.isEmpty() || !type3.isEmpty())
				{
					sb.append(isSynRestr ? VnRestrsXmlProcessor.START_SYNRESTR : VnRestrsXmlProcessor.START_SELRESTR);
					sb.append(value3);
					sb.append(type3);
					sb.append(isSynRestr ? VnRestrsXmlProcessor.END_SYNRESTR : VnRestrsXmlProcessor.END_SELRESTR);
				}
			}

			if (logic.isEmpty())
			{
				logic = "and";
			}
			if (k < nodes3.getLength() - 1)
			{
				sb.append(' ');
				sb.append(logic);
				sb.append(' ');
			}
		}
		return sb.toString();
	}
}
