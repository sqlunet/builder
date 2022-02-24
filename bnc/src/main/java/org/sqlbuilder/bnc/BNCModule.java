package org.sqlbuilder.bnc;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class BNCModule extends Module
{
	public static final String MODULE_ID = "bnc";

	protected BNCModule(final String conf, final Mode mode)
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
					new BNCProcessor(props).run();
					break;
				case RESOLVE:
					new BNCResolvingProcessor(props).run();
					break;
				case UPDATE:
					new BNCUpdatingProcessor(props).run();
					break;
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
		new BNCModule(conf, mode).run();
	}
}
