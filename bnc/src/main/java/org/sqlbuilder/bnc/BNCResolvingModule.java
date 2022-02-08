package org.sqlbuilder.bnc;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class BNCResolvingModule extends Module
{
	public static final String MODULE_ID = "bnc";

	protected BNCResolvingModule(final String conf)
	{
		super(MODULE_ID, conf);
	}

	@Override
	protected void run()
	{
		try
		{
			new BNCResolvingProcessor(props).run();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) throws IOException
	{
		new BNCResolvingModule(args[0]).run();
	}
}