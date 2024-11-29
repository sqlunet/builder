package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Utils;
import org.sqlbuilder.common.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import static org.sqlbuilder.common.Utils.camelCase;

public class VnSemanticsXmlProcessor extends XmlProcessor
{
	private static final boolean LOG_ONLY = false;
	private static final boolean TARGET_34 = true;

	static final char START_ARGS = '(';
	static final char END_ARGS = ')';
	static final String SEP_ARGS = ", ";
	static final String SEP_FORMULA = "\n";

	static final String EVENT_REGEX33 = "E[0-1]?";
	static final String EVENT_REGEX34 = "[eëE][0-8]?";
	static final String EVENT_ASPECT_REGEX = "(during|end|result|start)\\(" + (TARGET_34 ? EVENT_REGEX34 : EVENT_REGEX33) + "\\)";

	static final String SPECIFIC_REGEX33 = "Direction|Emotion|Endstate|Form|Instrument|Light|Material|Motion|Odor|Patient|Pos|Prep|Prop|Role|Sound|Theme|Theme_i|Theme_j|Weather_type";
	static final String SPECIFIC_REGEX34 = "V_Attribute|V_Configuration|V_Destination|V_Direction|V_Emotion|V_Eventuality|V_Final_State|V_Form|V_Injury|V_Instrument|V_Light|V_Material|V_Manner|V_Movement|V_Odor|V_Orientation|V_Patient|V_Position|V_Spatial_Relation|V_Sound|V_State|V_Theme|V_Trajectory|V_Weather";

	static final String CONSTANT_REGEX33 = "ch_of_loc|ch_of_poss|ch_of_state|ch_on_scale|deceptive|directedmotion|forceful|hostile|illegal|playful|quality|toward|tr_of_info";
	static final String CONSTANT_REGEX34 = "abstract|agent|manner|directedmotion|fast|forceful|from|illegal|importance|movement|physical|physical/abstract|toward|ch_of_loc|ch_of_poss|ch_of_state|ch_on_scale|tr_of_info|stimulated|stimulating|enticed|enticing|deceptive|deceived|hostile|playful|unserious|quality|negative_emotion|slow";

	static final Pattern EVENT_PATTERN = Pattern.compile(TARGET_34 ? EVENT_REGEX34 : EVENT_REGEX33, Pattern.CASE_INSENSITIVE);
	static final Pattern EVENT_ASPECT_PATTERN = Pattern.compile(EVENT_ASPECT_REGEX, Pattern.CASE_INSENSITIVE);
	static final Pattern SPECIFIC_PATTERN = Pattern.compile(TARGET_34 ? SPECIFIC_REGEX34 : SPECIFIC_REGEX33, Pattern.CASE_INSENSITIVE);
	static final Pattern CONSTANT_PATTERN = Pattern.compile(TARGET_34 ? CONSTANT_REGEX34 : CONSTANT_REGEX33, Pattern.CASE_INSENSITIVE);

	public VnSemanticsXmlProcessor()
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
		return VnSemanticsXmlProcessor.process(e);
	}

	private static String process(final Element root) throws RuntimeException
	{
		root.normalize();

		final StringBuilder sb = new StringBuilder();

		final NodeList preds = root.getElementsByTagName("PRED");
		final int m = preds.getLength();
		for (int i = 0; i < m; i++)
		{
			final Element pred = (Element) preds.item(i);

			final String neg = pred.getAttribute("bool").trim();
			if (!neg.isEmpty())
			{
				sb.append(neg);
			}

			final String value = pred.getAttribute("value").trim();
			sb.append(value);

			// args
			final NodeList argss = pred.getElementsByTagName("ARGS");
			if (argss.getLength() > 1)
			{
				throw new RuntimeException("ARGS number at pred " + i);
			}

			// args
			sb.append(VnSemanticsXmlProcessor.START_ARGS);
			int a = 0;
			final NodeList args = pred.getElementsByTagName("ARG");
			final int n = args.getLength();
			for (int j = 0; j < n; j++)
			{
				final Element arg = (Element) args.item(j);
				final String t = arg.getAttribute("type").trim();
				final String v = arg.getAttribute("value").trim();

				if (t.equalsIgnoreCase("ThemRole"))
				{
					if (a++ > 0)
					{
						sb.append(VnSemanticsXmlProcessor.SEP_ARGS);
					}
					sb.append(v);
				}
			}
			for (int j = 0; j < n; j++)
			{
				final Element arg = (Element) args.item(j);
				final String t = arg.getAttribute("type").trim();
				String v = arg.getAttribute("value").trim();
				if (!t.equalsIgnoreCase("ThemRole"))
				{
					if (a++ > 0)
					{
						sb.append(VnSemanticsXmlProcessor.SEP_ARGS);
					}

					// EVENT

					if (t.equalsIgnoreCase("Event"))
					{
						String v2 = v.charAt(0) == '?' ? v.substring(1) : v;
						// System.out.println("@EVENT " + v);
						if (EVENT_PATTERN.matcher(v2).matches())
						{
							v = v.toUpperCase(Locale.ENGLISH);
						}
						else if (EVENT_ASPECT_PATTERN.matcher(v2).matches())
						{
							v = camelCase(v);
						}
						else
						{
							if (LOG_ONLY)
							{
								System.err.println("SEMANTICS Event expression <" + v + ">");
							}
							else
							{
								throw new RuntimeException("SEMANTICS Event expression <" + v + ">");
							}
						}
						sb.append("event:").append(v);
					}

					// SPECIFIC

					else if (t.equalsIgnoreCase("VerbSpecific"))
					{
						String v2 = v.charAt(0) == '?' ? v.substring(1) : v;
						//System.out.println("@SPEC " + v);
						if (!SPECIFIC_PATTERN.matcher(v2).matches())
						{
							if (LOG_ONLY)
							{
								System.err.println("SEMANTICS VerbSpecific expression <" + v + ">");
							}
							else
							{
								throw new RuntimeException("SEMANTICS VerbSpecific expression <" + v + ">");
							}
						}
						sb.append("verbspecific:").append(v);
					}

					// C O N S T A N T

					else if (t.equalsIgnoreCase("Constant"))
					{
						//System.out.println("@CONST " + v);
						if (!CONSTANT_PATTERN.matcher(v).matches())
						{
							if (LOG_ONLY)
							{
								System.err.println("SEMANTICS Constant expression <" + v + ">");
							}
							else
							{
								throw new RuntimeException("SEMANTICS Constant expression <" + v + ">");
							}
						}
						sb.append("constant:").append(v);
					}
					else if (t.equalsIgnoreCase("PredSpecific"))
					{
					}
					else
					{
						throw new RuntimeException("type " + t);
					}
				}
			}
			sb.append(VnSemanticsXmlProcessor.END_ARGS);
			if (i < m - 1)
			{
				sb.append(VnSemanticsXmlProcessor.SEP_FORMULA);
			}
		}
		return sb.toString();
	}
}
