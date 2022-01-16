package org.sqlbuilder.vn;

import org.sqlbuilder.common.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class VnSemanticsXmlProcessor extends XmlProcessor
{
	static final char START_ARGS = '(';
	static final char END_ARGS = ')';
	static final String SEP_ARGS = ", ";
	static final String SEP_FORMULA = "\n";

	static final String EVENT = "(during|start|end|result)\\(E[01]?\\)";
	static final String SPECIFIC = "(Direction|Endstate|Emotion|Form|Instrument|\\??Material|Motion|Odor|\\??Patient|Pos|Prop|Role|Sound|\\?Theme|Theme_[ij]|Weather_type)";
	// "Direction"
	// "Endstate"
	// "Form"
	// "Material"
	// "Motion"
	// "Pos"
	// "Prep"
	// "Prop"
	static final String CONSTANT = "(abstract/physical|directedmotion|forceful|from|illegal|physical|physical/abstract|toward)";

	// "abstract"
	// "abstract/physical"
	// "directedmotion"
	// "forceful"
	// "from"
	// "illegal"
	// "physical"
	// "physical/abstract"
	// "toward"

	public VnSemanticsXmlProcessor()
	{
		//
	}

	@Override
	public String process(final String xml) throws ParserConfigurationException, SAXException, IOException
	{
		if (xml == null || xml.isEmpty())
			return null;
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

			final String neg = pred.getAttribute("bool");
			if (!neg.isEmpty())
			{
				sb.append(neg);
			}

			final String value = pred.getAttribute("value");
			sb.append(value);

			// args
			final NodeList argss = pred.getElementsByTagName("ARGS");
			if (argss.getLength() > 1)
				throw new RuntimeException("ARGS number at pred " + i);

			// args
			sb.append(VnSemanticsXmlProcessor.START_ARGS);
			int a = 0;
			final NodeList args = pred.getElementsByTagName("ARG");
			final int n = args.getLength();
			for (int j = 0; j < n; j++)
			{
				final Element arg = (Element) args.item(j);
				final String t = arg.getAttribute("type");
				final String v = arg.getAttribute("value");

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
				final String t = arg.getAttribute("type");
				final String v = arg.getAttribute("value");

				if (!t.equalsIgnoreCase("ThemRole"))
				{
					if (a++ > 0)
					{
						sb.append(VnSemanticsXmlProcessor.SEP_ARGS);
					}
					if (t.equalsIgnoreCase("Event"))
					{
						if (!v.matches("E[01]?") && !v.matches(VnSemanticsXmlProcessor.EVENT))
							throw new RuntimeException("Event expression " + v);
						sb.append("event:").append(v);
					}
					else if (t.equalsIgnoreCase("VerbSpecific"))
					{
						if (!v.matches(VnSemanticsXmlProcessor.SPECIFIC))
							throw new RuntimeException("VerbSpecific expression " + v);
						sb.append("verbspecific:").append(v);
					}
					else if (t.equalsIgnoreCase("Constant"))
					{
						if (!v.matches(VnSemanticsXmlProcessor.CONSTANT))
							throw new RuntimeException("Constant expression " + v);
						sb.append("constant:").append(v);
					}
					else
						throw new RuntimeException("type " + t);
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
