package org.sqlbuilder.sn;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class SnModule extends Module
{
	public static final String MODULE_ID = "sn";

	protected SnModule(final String conf, final Mode mode)
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
					new SnProcessor(props).run();
					break;

				case RESOLVE:
					new SnResolvingProcessor(props).run();
					break;

				case UPDATE:
					new SnUpdatingProcessor(props).run();
					break;

				case EXPORT:
				default:
					return;
			}
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
		new SnModule(conf, mode).run();
	}
}
