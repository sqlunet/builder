package org.sqlbuilder.pm;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class PmModule extends Module
{
	public static final String MODULE_ID = "pm";

	protected PmModule(final String conf, final Module.Mode mode)
	{
		super(MODULE_ID, conf, mode);
	}

	@Override
	protected void run()
	{
		assert props != null;

		try
		{
			switch (mode)
			{
				case PLAIN:
					new PmProcessor(props).run();
					break;

				case RESOLVE:
					new PmResolvingProcessor(props).run();
					break;

				case UPDATE:
					new PmUpdatingProcessor(props).run();
					break;

				case EXPORT:
				default:
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
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
		new PmModule(conf, mode).run();
	}
}
