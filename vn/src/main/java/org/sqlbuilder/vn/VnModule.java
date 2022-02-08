package org.sqlbuilder.vn;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.vn.collector.VnProcessor;

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
			new VnProcessor(props).run();
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
