package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FrameDocument.Frame;

public class FnFrame implements Insertable<FnFrame>
{
	public static final Set<FnFrame> SET = new HashSet<>();

	private static final FnFrameXmlProcessor definitionProcessor = new FnFrameXmlProcessor();

	public final Frame frame;

	public final String definition;

	public FnFrame(final Frame frame) throws IOException, SAXException, ParserConfigurationException
	{
		super();
		this.frame = frame;
		try
		{
			this.definition = FnFrame.definitionProcessor.process(this.frame.getDefinition());
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println(this.frame.getDefinition());
			throw e;
		}
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.frame.getID());
		// String(2, this.frame.getName());
		// String(3, this.definition);
		// String(4, this.frame.getCDate());
		// String(5, this.frame.getCBy());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[FRAME frid=%s name=%s]", this.frame.getID(), this.frame.getName());
	}
}
