package org.sqlbuilder.sn;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class SnModule extends Module
{
	public static final String MODULE_ID = "sn";

	private enum Type
	{PLAIN, RESOLVE, UPDATE}

	private final Type type;

	protected SnModule(final String conf, final Type type)
	{
		super(MODULE_ID, conf);
		this.type = type;
	}

	@Override
	protected void run()
	{
		try
		{
			switch (type)
			{
				case PLAIN:
					new SnProcessor(props).run();
					break;
				case RESOLVE:
					new SnResolvingProcessor(props).run();
					break;
				case UPDATE:
					new SnUpdatingProcessor(props).run();
					break;
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) throws IOException
	{
		int i = 0;
		Type type;
		switch (args[i])
		{
			case "-resolve":
				++i;
				type = Type.RESOLVE;
				break;
			case "-update":
				++i;
				type = Type.UPDATE;
				break;
			default:
				type = Type.PLAIN;
				break;
		}
		String conf = args[i];
		new SnModule(conf, type).run();
	}
}
