package org.sqlbuilder.sn;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class SnModule extends Module
{
	public static final String MODULE_ID = "sn";

	protected SnModule(final String conf)
	{
		super(MODULE_ID, conf);
	}

	@Override
	protected void run()
	{
		try
		{
			new SnProcessor(props).run();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) throws IOException
	{
		new SnModule(args[0]).run();
	}
}
