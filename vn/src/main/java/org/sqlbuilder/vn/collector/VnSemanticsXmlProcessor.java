package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

public class VnSemanticsXmlProcessor extends XmlProcessor
{
	private static final boolean LOG_ONLY = true;

	static final char START_ARGS = '(';
	static final char END_ARGS = ')';
	static final String SEP_ARGS = ", ";
	static final String SEP_FORMULA = "\n";

	static final String EVENT_REGEX = "(during|start|end|result)\\(E[0-8]?\\)";

	//static final String SPECIFIC = "Direction|Endstate|\\??Emotion|Form|Instrument|\\??Material|Motion|Odor|\\??Patient|Pos|Prop|Role|Sound|\\?Theme|Theme_[ij]|Weather_type|Light|\\??Prep|motion|prep|" +
	static final String SPECIFIC_REGEX = "V_Attribute|V_Configuration|V_Destination|V_Direction|V_Emotion|V_Eventuality|V_Final_State|V_Form|V_Injury|V_Instrument|V_Light|V_Material|V_Manner|V_Movement|V_Odor|V_Orientation|V_Patient|V_Position|V_Spatial_Relation|V_Sound|V_State|V_Theme|V_Trajectory|V_Weather";

	static final String CONSTANT_REGEX = "abstract|agent|manner|directedmotion|fast|forceful|from|illegal|importance|movement|physical|physical/abstract|toward|ch_of_loc|ch_of_poss|ch_of_state|ch_on_scale|tr_of_info|stimulated|stimulating|enticed|enticing|deceptive|deceived|hostile|playful|unserious|quality|negative_emotion|slow";

	static final Pattern EVENT_PATTERN = Pattern.compile(EVENT_REGEX, Pattern.CASE_INSENSITIVE);
	static final Pattern SPECIFIC_PATTERN = Pattern.compile(SPECIFIC_REGEX, Pattern.CASE_INSENSITIVE);
	static final Pattern CONSTANT_PATTERN = Pattern.compile(CONSTANT_REGEX, Pattern.CASE_INSENSITIVE);

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
				final String v = arg.getAttribute("value").trim();

				if (!t.equalsIgnoreCase("ThemRole"))
				{
					if (a++ > 0)
					{
						sb.append(VnSemanticsXmlProcessor.SEP_ARGS);
					}
					if (t.equalsIgnoreCase("Event"))
					{
						if (!v.matches("[eëE][0-8]?") && !EVENT_PATTERN.matcher(v).matches())
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
					else if (t.equalsIgnoreCase("VerbSpecific"))
					{
						if (!SPECIFIC_PATTERN.matcher(v).matches())
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
					else if (t.equalsIgnoreCase("Constant"))
					{
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
