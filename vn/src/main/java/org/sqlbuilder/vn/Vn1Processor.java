package org.sqlbuilder.vn;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

public class Vn1Processor extends VnProcessor
{
	public Vn1Processor(final Properties props)
	{
		super(props, "vn1");
	}

	@Override
	protected void run()
	{
		Progress.traceHeader("verbnet pass1", null);
		super.run();
	}

	@Override
	protected void processVerbNetClass(final VnDocument document, final Node start, final String head)
	{
		try
		{
			// class
			final String className = VnDocument.getClassName(start);
			final VnClass vnclass = new VnClass(className);
			VnClass.SET.add(vnclass);

			// get groupings
			final Collection<VnGrouping> groupings = VnDocument.getGroupings(start);
			VnGrouping.SET.addAll(groupings);

			// get role types
			final Collection<VnRoleType> roleTypes = VnDocument.getRoleTypes(start);
			VnRoleType.SET.addAll(roleTypes);

			// get selection restrs
			final Collection<VnRestrs> selRestrs = VnDocument.getSelRestrs(start);
			VnRestrs.SET.addAll(selRestrs);

			// get syntactic restrs
			final Collection<VnRestrs> synRestrs = VnDocument.getSynRestrs(start);
			VnRestrs.SET.addAll(synRestrs);

			// get selection restr types
			final Collection<VnRestrType> selRestrTypes = VnDocument.getSelRestrTypes(start);
			VnRestrType.SET.addAll(selRestrTypes);

			// get syntactic restr types
			final Collection<VnRestrType> synRestrTypes = VnDocument.getSynRestrTypes(start);
			VnRestrType.SET.addAll(synRestrTypes);

			// get frame names
			final Collection<VnFrameName> frameNames = VnDocument.getFrameNames(start);
			VnFrameName.SET.addAll(frameNames);

			// get frame subnames
			final Collection<VnFrameSubName> frameSubNames = VnDocument.getFrameSubNames(start);
			VnFrameSubName.SET.addAll(frameSubNames);

			// get frame examples
			final Collection<VnFrameExample> frameExamples = VnDocument.getFrameExamples(start);
			VnFrameExample.SET.addAll(frameExamples);

			// get syntaxes
			final Collection<VnSyntax> syntaxes = VnDocument.getSyntaxes(start);
			VnSyntax.SET.addAll(syntaxes);

			// get semantics
			final Collection<VnSemantics> semanticss = VnDocument.getSemantics(start);
			VnSemantics.SET.addAll(semanticss);

			// get predicates
			final Collection<VnPredicate> predicates = VnDocument.getPredicates(start);
			VnPredicate.SET.addAll(predicates);

			// recurse
			final NodeList subclasses = XPathUtils.getXPaths(start, "./SUBCLASSES/VNSUBCLASS");
			for (int i = 0; i < subclasses.getLength(); i++)
			{
				final Node subNode = subclasses.item(i);
				processVerbNetClass(document, subNode, head);
			}
		}
		catch (XPathExpressionException | TransformerException | ParserConfigurationException | SAXException | IOException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, this.tag, "read-class", document.getFileName(), -1, null, "xml", e);
		}
	}
}
