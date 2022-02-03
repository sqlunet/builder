package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;
import org.sqlbuilder.fn.collectors.FnFrameXmlProcessor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FrameDocument;

public class Frame implements HasID, Insertable
{
	public static final Set<Frame> SET = new HashSet<>();

	private static final FnFrameXmlProcessor definitionProcessor = new FnFrameXmlProcessor();

	public final int frameid;

	public final String name;

	public final String definition;

	public static Frame make(final FrameDocument.Frame frame) throws IOException, SAXException, ParserConfigurationException
	{
		var f = new Frame(frame);
		SET.add(f);
		return f;
	}

	private Frame(final FrameDocument.Frame frame) throws IOException, SAXException, ParserConfigurationException
	{
		this.frameid = frame.getID();
		this.name = frame.getName();
		try
		{
			this.definition = Frame.definitionProcessor.process(frame.getDefinition());
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println(frame.getDefinition());
			throw e;
		}
	}

	// A C C E S S

	public long getID()
	{
		return frameid;
	}

	public String getName()
	{
		return name;
	}

	public String getDefinition()
	{
		return definition;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Frame that = (Frame) o;
		return frameid == that.frameid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(frameid);
	}

	// O R D E R

	public static final Comparator<Frame> COMPARATOR = Comparator.comparing(Frame::getName).thenComparing(Frame::getID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s'", //
				frameid, //
				Utils.escape(name), //
				Utils.escape(definition));
		// frame.getCDate()
		// frame.getCBy()
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FRAME id=%s name=%s]", frameid, name);
	}
}
