package org.sqlbuilder.sumo;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.common.NotFoundException;

import java.io.IOException;

public class SumoModule extends Module
{
	public static final String MODULE_ID = "sumo";

	protected SumoModule(final String conf, final Mode mode)
	{
		super(MODULE_ID, conf, mode);
	}

	@Override
	protected void run()
	{
		assert props != null;

		try
		{
			switch (mode)
			{
				case PLAIN:
					new SumoProcessor(props).run();
					break;

				case RESOLVE:
					new SumoResolvingProcessor(props).run();
					break;

				case UPDATE:
					new SumoUpdatingProcessor(props).run();
					break;

				case EXPORT:
				default:
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void turnOffLogging()
	{
		final String pathKey = "java.util.logging.config.file";
		final String pathValue = "logging.properties";
		System.setProperty(pathKey, pathValue);

		final String classKey = "java.util.logging.config.class";
		final String classValue = System.getProperty(classKey);
		if (classValue != null && !classValue.isEmpty())
		{
			System.err.println(classKey + " = " + classValue);
		}
	}

	public static void main(String[] args) throws NotFoundException
	{
		turnOffLogging();

		int i = 0;
		Mode mode = Mode.PLAIN;
		if (args[i].startsWith("-"))
		{
			mode = Mode.read(args[i++]);
		}
		String conf = args[i];
		new SumoModule(conf, mode).run();
	}
}
