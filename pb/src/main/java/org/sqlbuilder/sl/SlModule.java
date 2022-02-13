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

	protected SlModule(final String conf, final Mode mode)
	{
		super(MODULE_ID, conf, mode);
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
		int i = 0;
		Mode mode = Mode.PLAIN;
		if (args[i].startsWith("-"))
		{
			mode = Mode.read(args[i++]);
		}
		String conf = args[i];
		new SlModule(conf, mode).run();
	}
}
