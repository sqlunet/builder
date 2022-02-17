package org.sqlbuilder.fn;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.fn.collectors.*;
import org.sqlbuilder.fn.objects.Word;

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

		switch(mode)
		{
			case PLAIN:
			case RESOLVE:
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
				break;

			case UPDATE:
				new FnWordProcessor(props).run();
				try
				{
					Inserter inserter = new ResolvingUpdater(props);
					inserter.insert();
				}
				catch (IOException | ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				break;

			case EXPORT:
				new FnExportingProcessor(props).run();
				try
				{
					Exporter exporter = new Exporter(props);
					exporter.run();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;

			default:
				return;
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
