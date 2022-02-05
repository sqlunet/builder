package org.sqlbuilder.pb;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.pb.collectors.PbProcessor;

import java.io.IOException;
import java.util.Properties;

public class PbModule extends Module
{
	public static final String MODULE_ID = "pb";

	protected PbModule(final String conf)
	{
		super(MODULE_ID, conf);
	}

	@Override
	protected void run()
	{
		try
		{
			buildPropBank(props);
			new Inserter(props).insert();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void buildPropBank(final Properties props) throws IOException
	{
		new PbProcessor(props).run();
	}

	public static void main(final String[] args)
	{
		new PbModule(args[0]).run();
	}
}
