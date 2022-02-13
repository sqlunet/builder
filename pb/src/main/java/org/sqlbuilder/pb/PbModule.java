package org.sqlbuilder.pb;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.pb.collectors.PbProcessor;
import org.sqlbuilder.sl.collectors.SemlinkProcessor;

import java.io.IOException;
import java.util.Properties;

public class PbModule extends Module
{
	public static final String MODULE_ID = "pb";

	protected PbModule(final String conf, final Mode mode)
	{
		super(MODULE_ID, conf, mode);
	}

	@Override
	protected void run()
	{
		buildPropBank(props);
		buildSemLink(props);
		try
		{
			new Inserter(props).insert();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void buildPropBank(final Properties props)
	{
		new PbProcessor(props).run();
	}

	private static void buildSemLink(final Properties props)
	{
		new SemlinkProcessor(props).run();
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
		new PbModule(conf, mode).run();
	}
}
