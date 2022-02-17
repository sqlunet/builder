package org.sqlbuilder.vn;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.vn.collector.VnExportingProcessor;
import org.sqlbuilder.vn.collector.VnProcessor;
import org.sqlbuilder.vn.collector.VnUpdatingProcessor;

import java.io.IOException;

public class VnModule extends Module
{
	public static final String MODULE_ID = "vn";

	public VnModule(final String conf, final Mode mode)
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
				new VnProcessor(props).run();
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
				new VnUpdatingProcessor(props).run();
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
				new VnExportingProcessor(props).run();
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
		new VnModule(conf, mode).run();
	}
}
