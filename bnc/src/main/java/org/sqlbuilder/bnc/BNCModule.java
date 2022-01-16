package org.sqlbuilder.bnc;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class BNCModule extends Module
{
	public static final String MODULE_ID = "bnc";

	protected BNCModule(final String conf)
	{
		super(MODULE_ID, conf);
	}

	@Override
	protected void run()
	{
		try
		{
			new BNCProcessor(props).run();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) throws IOException
	{
		new BNCModule(args[0]).run();
	}
}
