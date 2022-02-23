package org.sqlbuilder.sl;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.sl.collectors.SemlinkProcessor;

import java.io.IOException;

public class SlModule extends Module
{
	public static final String MODULE_ID = "sl";

	protected SlModule(final String conf, final Mode mode)
	{
		super(MODULE_ID, conf, mode);
	}

	@Override
	protected void run()
	{
		assert props != null;

		new SemlinkProcessor(props).run();
		switch (mode)
		{
			case PLAIN:
			case RESOLVE:
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
		new SlModule(conf, mode).run();
	}
}
