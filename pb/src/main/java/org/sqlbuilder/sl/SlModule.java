package org.sqlbuilder.sl;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.pb.Inserter;
import org.sqlbuilder.sl.collectors.Semlink0Processor;
import org.sqlbuilder.sl.collectors.Semlink1Processor;
import org.sqlbuilder.sl.collectors.Semlink2Processor;
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
			new Inserter(props).insert();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	private static void initSemLink(final Properties props)
	{
		new Semlink0Processor(props).run();
	}

	private static void buildSemLink(final Properties props) throws FileNotFoundException
	{
		SlModule.initSemLink(props);
		new SemlinkProcessor().run();
	}

	private static void buildSemLink1(final Properties props)
	{
		SlModule.initSemLink(props);
		new Semlink1Processor().run();
	}

	private static void buildSemLink2(final Properties props)
	{
		SlModule.initSemLink(props);
		new Semlink2Processor().run();
	}

	public static void main(final String[] args)
	{
		new SlModule(args[0]).run();
	}
}
