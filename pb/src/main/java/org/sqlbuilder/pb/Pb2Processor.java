package org.sqlbuilder.pb;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.Properties;

import javax.xml.xpath.XPathExpressionException;

public class Pb2Processor extends PbProcessor
{
	public Pb2Processor(final Properties props)
	{
		super(props, "pb2");
	}

	@Override
	protected void run()
	{
		Progress.traceHeader("propbank pass2", null);
		super.run();
		Progress.traceTailer("propbank pass2", 0);
	}

	@Override
	protected int processFrameset(final PbVerbDocument document, final Node start, final String head)
	{
		long count = 0;
		try
		{
			// examples
			final Collection<PbExample> examples = PbVerbDocument.getExamples(head, start);
			if (examples != null)
			{
				PbExample.SET.addAll(examples);
			}
		}
		catch (XPathExpressionException e)
		{
			Logger.instance.logXmlException(PbModule.MODULE_ID, this.tag, "example", document.getFileName(), -1, null, "xpath", e);
		}
		return (int) count;
	}
}
