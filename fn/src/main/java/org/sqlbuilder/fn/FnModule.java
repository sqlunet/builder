package org.sqlbuilder.fn;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.fn.collectors.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FnModule extends Module
{
	public static final String MODULE_ID = "fn";

	protected FnModule(final String conf, final Mode mode)
	{
		super(MODULE_ID, conf, mode);
	}

	@Override
	protected void run()
	{
		assert props != null;

		new FnEnumProcessor().run();
		new FnSemTypeProcessor("semTypes.xml", props).run();
		new FnFrameProcessor(props).run();
		new FnLexUnitProcessor(props).run();
		new FnFullTextProcessor(props).run();

		try
		{
			Inserter inserter = mode == Mode.PLAIN ? new Inserter(props) : new ResolvingInserter(props);
			inserter.insert();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) throws IOException
	{
		int i = 0;
		Mode mode = Mode.PLAIN;
		if (args[i].startsWith("-"))
		{
			mode = Mode.read(args[i++]);
		}
		String conf = args[i];
		new FnModule(conf, mode).run();
	}
}
