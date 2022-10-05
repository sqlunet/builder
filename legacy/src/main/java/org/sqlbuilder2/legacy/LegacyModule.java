package org.sqlbuilder2.legacy;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class LegacyModule extends Module
{
	public static final String MODULE_ID = "legacy";

	private final String[] args;

	protected LegacyModule(final String[] args)
	{
		super(MODULE_ID, args[0], null);
		this.args = args;
	}

	@Override
	protected void run()
	{
		assert props != null;
		for (int i = 2; i < args.length; i++)
		{
			if (args[i].startsWith("from="))
			{
				props.setProperty("from", args[i].substring(5));
			}
			if (args[i].startsWith("to="))
			{
				props.setProperty("to", args[i].substring(3));
			}
		}

		try
		{
			switch (args[1])
			{
				case "synsets":
					new SynsetToSynsetProcessor(props).run();
					break;

				case "sensekeys":
					new SenseToSensekeyProcessor(props).run();
					break;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args)
	{
		new LegacyModule(args).run();
	}
}
