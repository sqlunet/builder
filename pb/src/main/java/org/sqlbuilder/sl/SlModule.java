package org.sqlbuilder.sl;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.pb.Inserter;
import org.sqlbuilder.sl.collectors.SemlinkProcessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SlModule extends Module
{
	public static final String MODULE_ID = "sl";

	protected SlModule(final String conf)
	{
		super(MODULE_ID, conf);
	}

	@Override
	protected void run()
	{
		try
		{
			buildSemLink(props);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	private static void buildSemLink(final Properties props) throws FileNotFoundException
	{
		new SemlinkProcessor(props).run();
	}

	public static void main(final String[] args)
	{
		new SlModule(args[0]).run();
	}
}
