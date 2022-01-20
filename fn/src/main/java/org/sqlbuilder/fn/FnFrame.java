package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FrameDocument.Frame;

public class FnFrame implements HasID, Insertable<FnFrame>
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
			this.definition = FnFrame.definitionProcessor.process(frame.getDefinition());
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println(frame.getDefinition());
			throw e;
		}
	}

	public long getID()
	{
		return frame.getID();
	}

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s'", //
				frame.getID(), //
				Utils.escape(frame.getName()), //
				Utils.escape(definition));
		// String(4, this.frame.getCDate());
		// String(5, this.frame.getCBy());
	}

	@Override
	public String toString()
	{
		return String.format("[FRAME frid=%s name=%s]", frame.getID(), frame.getName());
	}
}
