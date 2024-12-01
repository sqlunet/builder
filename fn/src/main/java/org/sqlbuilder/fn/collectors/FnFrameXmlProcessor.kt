package org.sqlbuilder.fn.collectors;

import org.sqlbuilder.common.XmlChecker;
import org.sqlbuilder.common.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class FnFrameXmlProcessor extends XmlProcessor
{
	private static final boolean LOG_ONLY = false;

	public FnFrameXmlProcessor()
	{
		//
	}

	@Override
	public String process(final String xml) throws ParserConfigurationException, SAXException, IOException
	{
		final Element e = XmlProcessor.docFromString("<root>" + xml + "</root>");
		return FnFrameXmlProcessor.process(e);
	}

	private static String process(final Element root) throws RuntimeException
	{
		root.normalize();

		final StringBuilder sb = new StringBuilder();

		final NodeList roots = root.getElementsByTagName("def-root");
		for (int i = 0; i < roots.getLength(); i++)
		{
			final Element e = (Element) roots.item(i);

			final NodeList nodes = e.getChildNodes();
			for (int j = 0; j < nodes.getLength(); j++)
			{
				final Node node = nodes.item(j);
				if (!(node instanceof Element))
				{
					String value = node.getNodeValue();
					value = value.replaceAll("\n\n+", "\n");
					sb.append(value);
				}
				else
				{
					final Element e2 = (Element) node;
					String name2 = node.getNodeName();

					// fix
					if ("fex".equals(name2))
					{
						e2.getOwnerDocument().renameNode(node, null, "fen");
						e2.removeAttribute("name");
						name2 = node.getNodeName();
					}
					else if ("m".equals(name2))
					{
						e2.getOwnerDocument().renameNode(node, null, "em");
						name2 = node.getNodeName();
					}
					XmlChecker.checkElementName(name2, "(fen|t|ex|ment|em)", "top");

					switch (name2)
					{
						case "ex":
							XmlChecker.checkAttributeName(e2, null, name2, LOG_ONLY);

							final String example = FnFrameXmlProcessor.processExample(e2);
							sb.append("<ex>").append(example).append("</ex>");
							break;
						case "fen":
						{
							XmlChecker.checkSubElements(e2, null, name2, LOG_ONLY);
							XmlChecker.checkAttributeName(e2, null, name2, LOG_ONLY);

							final String value = e2.getTextContent().replaceAll("\n\n+", "\n");
							sb.append("<fen>").append(value).append("</fen>");
							break;
						}
						case "t":
						{
							XmlChecker.checkSubElements(e2, null, name2, LOG_ONLY);
							XmlChecker.checkAttributeName(e2, null, name2, LOG_ONLY);

							final String value = e2.getTextContent().replaceAll("\n\n+", "\n");
							sb.append("<t>").append(value).append("</t>");
							break;
						}
						case "ment":
						{
							// TODO only one error
							// XmlChecker.check_SubElements(e2, null, name2);
							XmlChecker.checkSubElements(e2, "(fen)", name2, LOG_ONLY);
							XmlChecker.checkAttributeName(e2, null, name2, LOG_ONLY);

							final String value = e2.getTextContent().replaceAll("\n\n+", "\n");
							sb.append(value);
							// System.err.println(name2 + " " + value);
							break;
						}
						case "em":
						{
							XmlChecker.checkSubElements(e2, null, name2, LOG_ONLY);
							XmlChecker.checkAttributeName(e2, null, name2, LOG_ONLY);

							final String value = e2.getTextContent().replaceAll("\n\n+", "\n");
							sb.append(value);
							// System.err.println(name2);
							break;
						}
					}
				}
			}
		}
		return sb.toString() //
				.replace("<ex></ex>", "").replaceAll("\\s\\s+", " ")
				// .replaceAll("\n\n+", "\n")
				.replaceAll("\n+", "");
	}

	private static String processExample(final Element root) throws RuntimeException
	{
		root.normalize();

		final StringBuilder sb = new StringBuilder();

		final NodeList nodes = root.getChildNodes();
		for (int j = 0; j < nodes.getLength(); j++)
		{
			final Node node = nodes.item(j);
			if (!(node instanceof Element))
			{
				String value = node.getNodeValue();
				value = value.replaceAll("\n\n+", "\n");
				sb.append(value);
			}
			else
			{
				final Element e2 = (Element) node;
				final String name2 = node.getNodeName();
				XmlChecker.checkElementName(name2, "(fex|t|m|ment|gov|x|target)", "ex");

				switch (name2)
				{
					case "fex":
					{
						XmlChecker.checkSubElements(e2, "(t)", name2, LOG_ONLY);
						XmlChecker.checkAttributeName(e2, "(name)", name2, LOG_ONLY);

						// attribute
						final String attr = e2.getAttribute("name");
						// XmlChecker.check_AttributeValue(
						// attr,
						// "(act|Actor|Address|Addressee|Affected_party|Age|Aggregate|Alterant|Area|ass|Assessor|Audience|Bad_entity|Basis|bed|beh|Benefited_party|Body_location|Category|Cause|Change_agent|Characteristic|Charges|Chosen|Chrg|ci|cir|Circumstances|clo|Clothing|Code|Cognizer_1|col|com|Commitment|Communicator|Completeness|Concept_1|Condition|Connections|Consideration|Containing_object|Contents|Contrast_set|Cr|CrEnt|Criterion|Crop|dan|dan_ent|Danger|Deceased|Decision|DefEvnt|dep|depe|Depictive|deps|Desirable_action|Desirable_situation|Determinant|Deviation|Dimension|dir|dis|Distance|Documents|dom|Domain|Dryee|dur|dut|Duty|Earnings|eff|Ego|el|Emotion|Enabled_action|Enabled_situation|Entry_path|est|eva|Eval|Evaluation|Eventuality|Evt|exe|Existing_member|exp|Factor|Father|fen|Final_value|finc|Fine|Firearm|fix|Flammables|Foc|Focal_occasion|Focal_participant|Food|Force|Frequency|fun|Game|goa|Goal|Goods|gov|Grantee|gro|Hair|Harmful_event|Hidden_object|Hindrance|Holding_Location|hos|Id|Idea|Impactee|Inc|Incident|ind|ind2|inis|Initial_element|Initial_state|Injury|Inst|Insulator|Intended_event|Intended_referent|Interlocutor_2|Intermediary|iss|Issue|I_state|I-state|ite|Item-2|Knot|lab|Landmark|Landmark_event|Liquid|loc|Main_statement|man|Manner|mea|Means|Measurement|Medium|Member|men|Message|mis|mns|Msg|nam|Name_source|neg|New_duration|New_idea|New_leader|Number_of_possibilities|Obligation|Obstruction|Occupant|Official|old|Old|oo|opi|Original|Other|par|Part_2|Partner_2|Part_prop|Party_2|Pat|Path|Patient|Period|Phen|Phenomenon_2|Pieces|pl|pla|Plan|Posit|Position|Possession|Prize|Process|Production|Property|Proposition|pur|que|Rank|rat|Represented|Requirement|Resistant_surface|Resource|Response|Responsible_party|Resulting_action|Roadway|rol|Role|Rope|Rule|ser|Sev|Severity|Shine|sig|Simulated_entity|Sit|Size|Size_change|Skill|soc|Society|Sought_entity|Sound_maker|Source|Source_representation|Spd|Speed|src|Standard_item|State|stu|Stuff|Subject|Subset|Sub_source|Supporter|System|Target|Target_location|Task|tem|Temperature_goal|Term2|tex|the|Theme|thm1|ti|Time|toc|top|tos|Total|toxs|Traveller|Typ|Undesirable_location|User|val|Value|Variable|Weapon|Witness)",
						// "fex");

						final String value = e2.getTextContent().replaceAll("\n\n+", "\n");
						sb.append("<fex name='").append(attr).append("'>").append(value).append("</fex>");
						break;
					}
					case "t":
					{
						XmlChecker.checkSubElements(e2, "(fex)", name2, LOG_ONLY);
						XmlChecker.checkAttributeName(e2, null, name2, LOG_ONLY);

						final String value = e2.getTextContent().replaceAll("\n\n+", "\n");
						sb.append('<').append(name2).append('>').append(value).append("</").append(name2).append('>');
						break;
					}
					case "x":
					{
						XmlChecker.checkSubElements(e2, "(fex|t)", "x", LOG_ONLY);
						XmlChecker.checkAttributeName(e2, null, name2, LOG_ONLY);

						final String value = e2.getTextContent().replaceAll("\n\n+", "\n");
						sb.append('<').append(name2).append('>').append(value).append("</").append(name2).append('>');
						break;
					}
					/*
					 * case "gov": { XmlChecker.checkSubElements(e2, null, name2); XmlChecker.checkAttributeName(e2, null, name2);
					 *
					 * final String value = e2.getTextContent().replaceAll("\n\n+", "\n"); // sb.append('<').append(name2).append('>').append(value).append("</").append(name2).append('>'); sb.append(value); break; }
					 */
					default:
					{
						XmlChecker.checkSubElements(e2, null, name2, LOG_ONLY);
						XmlChecker.checkAttributeName(e2, null, name2, LOG_ONLY);

						final String value = e2.getTextContent().replaceAll("\n\n+", "\n");
						// sb.append('<').append(name2).append('>').append(value).append("</").append(name2).append('>');
						sb.append(value);
						break;
					}
				}
			}
		}
		return sb.toString();
	}
}
