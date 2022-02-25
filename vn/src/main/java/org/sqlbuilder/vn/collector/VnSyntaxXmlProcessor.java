package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Checker;
import org.sqlbuilder.common.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class VnSyntaxXmlProcessor extends XmlProcessor
{
	static final String SEP_CAT = "\n";
	static final String START_SELRESTR = " <";
	static final char END_SELRESTR = '>';
	static final String START_SYNRESTR = " {";
	static final char END_SYNRESTR = '}';

	static final String SYNRESTR = "(ac_ing|ac_to_inf|adv_loc|be_sc_ing|body_part|definite|for_comp|genitive|how_extract|np_ing|np_omit_ing|np_p_ing|np_ppart|np_to_be|np_to_inf|oc_bare_inf|oc_ing|oc_to_inf|plural|pos_ing|poss_ing|possing|quotation|refl|rs_to_inf|sc_ing|sc_to_inf|sentential|small_clause|tensed_that|that_comp|to_be|to_inf_rs|what_extract|vc_to_inf|wh_comp|wh_inf|what_inf|wheth_inf)";
	// xsd
	// "ac_bare_inf"
	// "ac_ing"
	// "ac_to_inf"
	// "adv_loc"
	// "bare_inf"
	// "be_ing_sc"
	// "control_bare_inf"
	// "control_ing"
	// "control_to_inf"
	// "definite"
	// "extracted"
	// "for_comp"
	// "genitive"
	// "gerund"
	// "how_extract"
	// "how_inf"
	// "if_comp"
	// "indicative"
	// "infinitival"
	// "np_omit_ing"
	// "null_comp"
	// "oc_bare_inf"
	// "oc_ing"
	// "oc_to_inf"
	// "plural"
	// "poss"
	// "poss_ing"
	// "poss_ing_sc"
	// "ppart"
	// "quotation"
	// "refl"
	// "rs_bare_inf"
	// "rs_to_inf"
	// "sc_ing"
	// "sc_to_inf"
	// "sentential"
	// "small_clause"
	// "tensed_that"
	// "that_comp"
	// "to_inf"
	// "vc_to_inf"
	// "what_extract"
	// "what_inf"
	// "wh_comp"
	// "when_extract"
	// "when_inf"
	// "where_extract"
	// "where_inf"
	// "wheth_comp"
	// "wheth_inf"
	// "wh_inf"
	// "who_extract"
	// "why_extract"

	static final String SELRESTR = "(body_part|concrete|dest|dest_conf|dest_dir|dir|loc|location|path|region|refl|src|spatial|state)";

	// xsd
	// "abstract"
	// "animal"
	// "animate"
	// "artifact"
	// "body_part"
	// "comestible"
	// "communication"
	// "company"
	// "concrete"
	// "concrete"
	// "currency"
	// "elongated"
	// "force"
	// "garment"
	// "human"
	// "idea"
	// "int_control"
	// "location"
	// "machine"
	// "natural"
	// "nonrigid"
	// "organization"
	// "phys_obj"
	// "place"
	// "plant"
	// "plural"
	// "pointy"
	// "quotation"
	// "refl"
	// "region"
	// "rigid"
	// "scalar"
	// "solid"
	// "sound"
	// "state"
	// "substance"
	// "time"
	// "tool"
	// "vehicle"

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
				Checker.checkEmpty(node, "root");
				continue;
			}
			final Element e = (Element) node;

			// name
			final String name = e.getNodeName();
			Checker.checkElementName(name, "ADJ|ADV|LEX|NP|PREP|VERB", name);
			sb.append(name);

			// attribute check
			Checker.checkAttributeName(e, "value", name);

			// 'value' attribute value
			final String value = e.getAttribute("value");
			if (!value.isEmpty())
			{
				Checker.checkAttributeValue(value, //
						"('s|about|after|against|among|and|apart|as|at|away|be|between|in_between|\\[\\+be\\]|before|by|concerning|down|for|from|in|into|it|it\\[\\+be\\]|like|of|off|on|onto|out|out_of|over|regarding|respecting|there|through|to|together|towards|under|until|up|upon|with|" + //
								"Agent|Asset|Attribute|Beneficiary|Cause|Co-Agent|Co-Patient|Co-Theme|Destination|Experiencer|Extent|Goal|Initial_Location|Instrument|Location|Material|Patient|Pivot|Predicate|Product|Recipient|Reflexive|Result|Source|Stimulus|Theme|Time|Topic|Trajectory|Value)", name);
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
					Checker.checkEmpty(node2, name);
					continue;
				}
				final Element e2 = (Element) node2;

				// name
				final String name2 = e2.getNodeName();
				Checker.checkElementName(name2, "SELRESTRS|SYNRESTRS", name2);
				// sb.append(" = ");
				// sb.append(name2);

				// attribute check
				Checker.checkAttributeName(e2, "logic", name2);

				// 'Value' attribute value
				String logic = e2.getAttribute("logic");
				if (!logic.isEmpty())
				{
					Checker.checkAttributeValue(logic, "or", name2);
				}

				// SELRESTR|SYNRESTR
				final NodeList nodes3 = node2.getChildNodes();
				for (int k = 0; k < nodes3.getLength(); k++)
				{
					final Node node3 = nodes3.item(k);
					if (!(node3 instanceof Element))
					{
						Checker.checkEmpty(node3, name2);
						continue;
					}
					final Element e3 = (Element) node3;

					// name
					final String name3 = e3.getNodeName();
					final boolean isSynRestr = name3.equals("SYNRESTR");
					final boolean isSelRestr = name3.equals("SELRESTR");

					Checker.checkElementName(name3, "SELRESTR|SYNRESTR", name3);
					// sb.append(" - ");
					// sb.append(name3);

					// attribute check
					Checker.checkAttributeName(e3, "Value|type", name3);

					// 'Value' attribute value
					final String value3 = e3.getAttribute("Value");
					if (!value3.isEmpty())
					{
						Checker.checkAttributeValue(value3, "(\\+|\\-)", name3);
					}

					// 'type' attribute value
					final String type3 = e3.getAttribute("type");
					if (!type3.isEmpty())
					{
						if (isSynRestr)
						{
							Checker.checkAttributeValue(type3, VnSyntaxXmlProcessor.SYNRESTR, name3);
						}
						if (isSelRestr)
						{
							Checker.checkAttributeValue(type3, VnSyntaxXmlProcessor.SELRESTR, name3);
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
