package org.sqlbuilder.fn;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.fn.collectors.*;

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

		switch (mode)
		{
			case PLAIN:
			case RESOLVE:
				new FnEnumCollector().run();
				new FnSemTypeCollector("semTypes.xml", props).run();
				new FnFrameCollector(props).run();
				new FnLexUnitCollector(props).run();
				new FnFullTextCollector(props).run();
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
				new FnWordCollector(props).run();
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
				new FnExportCollector(props).run();
				new FnWordCollector(props).run();
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
		}
	}

	public static void main(final String[] args)
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
