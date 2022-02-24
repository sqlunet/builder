package org.sqlbuilder.pb;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.pb.collectors.PbExportCollector;
import org.sqlbuilder.pb.collectors.PbCollector;
import org.sqlbuilder.pb.collectors.PbUpdateCollector;

import java.io.IOException;

public class PbModule extends Module
{
	public static final String MODULE_ID = "pb";

	protected PbModule(final String conf, final Mode mode)
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
				new PbCollector(props).run();
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
				new PbUpdateCollector(props).run();
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
				new PbExportCollector(props).run();
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
		new PbModule(conf, mode).run();
	}
}
