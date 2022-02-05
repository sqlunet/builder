package org.sqlbuilder.sl.collectors;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.XmlDocument;
import org.sqlbuilder.pb.PbModule;
import org.sqlbuilder.pb.foreign.PbRole_VnRole;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class Semlink0Processor extends Processor
{
	protected final String semlinkFile;

	public Semlink0Processor(final Properties props)
	{
		super("semlink0");
		this.semlinkFile = props.getProperty("semlinkhome", System.getenv().get("SEMLINKHOME")) + File.separatorChar + props.getProperty("semlinkfile");
	}

	@Override
	public void run()
	{
		if (Logger.verbose)
		{
			Progress.traceHeader("semlink " + this.semlinkFile, "");
		}
		try
		{
			final SemlinkDocument document = new SemlinkDocument(this.semlinkFile);
			Semlink0Processor.processSemlinks(XmlDocument.getXPath(document.getDocument(), "./pbvn-typemap"));
		}
		catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e)
		{
			Logger.instance.logXmlException(PbModule.MODULE_ID, this.tag, "xml-document", this.semlinkFile, -1, null, "document=[" + this.semlinkFile + "]", e);
		}

		if (Logger.verbose)
		{
			Progress.traceTailer("semlink ", "");
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	protected static int processSemlinks(final Node start) throws XPathExpressionException
	{
		Progress.traceHeader("semlink file", "reading");
		PbRole_VnRole.semlinkMap = SemlinkDocument.getMappings(start);
		Progress.traceTailer("semlink files", "");
		return PbRole_VnRole.semlinkMap.size();
	}
}
