package org.sqlbuilder.bnc;

import org.sqlbuilder.common.Module;

import java.io.IOException;

public class BNCModule extends Module
{
	public static final String MODULE_ID = "bnc";

	private enum Type
	{PLAIN, RESOLVE, UPDATE}

	private final Type type;

	protected BNCModule(final String conf, final Type type)
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
					new BNCProcessor(props).run();
					break;
				case RESOLVE:
					new BNCResolvingProcessor(props).run();
					break;
				case UPDATE:
					new BNCUpdatingProcessor(props).run();
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
		new BNCModule(conf, type).run();
	}
}
