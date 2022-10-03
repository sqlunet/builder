package org.sqlbuilder.sumo;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.common.NotFoundException;

import java.io.IOException;

public class SumoModule extends Module
{
	public static final String MODULE_ID = "sumo";

	protected SumoModule(final String conf, final Mode mode)
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
					new SumoProcessor(props).run();
					break;

				case RESOLVE:
					new SumoResolvingProcessor(props).run();
					break;

				case UPDATE:
					new SumoUpdatingProcessor(props).run();
					break;

				case EXPORT:
				default:
			}
		}
		catch (IOException e)

		{
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws NotFoundException
	{
		int i = 0;
		Mode mode = Mode.PLAIN;
		if (args[i].startsWith("-"))
		{
			mode = Mode.read(args[i++]);
		}
		String conf = args[i];
		new SumoModule(conf, mode).run();
	}
}
