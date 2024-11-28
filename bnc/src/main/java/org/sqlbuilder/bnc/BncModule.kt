package org.sqlbuilder.bnc;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class BncModule extends Module
{
	public static final String MODULE_ID = "bnc";

	protected BncModule(final String conf, final Mode mode)
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
					new BncProcessor(props).run();
					break;
				case RESOLVE:
					new BncResolvingProcessor(props).run();
					break;
				case UPDATE:
					new BncUpdatingProcessor(props).run();
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
		new BncModule(conf, mode).run();
	}
}
