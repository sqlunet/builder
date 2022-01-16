package org.sqlbuilder.vn;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class VnModule extends Module
{
	public static final String MODULE_ID = "vn";

	public VnModule(final String conf)
	{
		super(MODULE_ID, conf);
	}

	@Override
	protected void run()
	{
		try
		{
			new Vn1Processor(props).run();
			new Vn2Processor(props).run();
			new Vn3Processor(props).run();
			new Vn4Processor(props).run();
			new Inserter(props).insert();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) throws IOException
	{
		new VnModule(args[0]).run();
	}
}
