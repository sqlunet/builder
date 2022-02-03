package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
import org.sqlbuilder.vn.VnDocument;
import org.sqlbuilder.vn.VnModule;
import org.sqlbuilder.vn.objects.*;
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
	public void run()
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
			VnClass.make(className);

			// get groupings
			VnDocument.makeGroupings(start);

			// get role types
			VnDocument.makeRoleTypes(start);

			// get selection restrs
			VnDocument.makeSelRestrs(start);

			// get syntactic restrs
			VnDocument.makeSynRestrs(start);

			// get selection restr types
			VnDocument.makeSelRestrTypes(start);

			// get syntactic restr types
			VnDocument.makeSynRestrTypes(start);

			// get frame names
			VnDocument.makeFrameNames(start);

			// get frame subnames
			VnDocument.makeFrameSubNames(start);

			// get frame examples
			VnDocument.makeFrameExamples(start);

			// get syntaxes
			final Collection<VnSyntax> syntaxes = VnDocument.makeSyntaxes(start);

			// get semantics
			final Collection<VnSemantics> semanticss = VnDocument.makeSemantics(start);

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
