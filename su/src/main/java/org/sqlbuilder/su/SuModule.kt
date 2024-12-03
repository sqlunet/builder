package org.sqlbuilder.su;

import com.articulate.sigma.Logging;
import com.articulate.sigma.Nullable;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.common.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class SuModule extends Module
{
	public static final String MODULE_ID = "sumo";

	protected SuModule(final String conf, final Mode mode)
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
					new SuProcessor(props).run();
					break;

				case RESOLVE:
					new SuResolvingProcessor(props).run();
					break;

				case UPDATE:
					new SuUpdatingProcessor(props).run();
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

	private static void setLogging()
	{
		try (@Nullable InputStream is = Logging.class.getClassLoader().getResourceAsStream("logging.properties"))
		{
			//Properties props = new Properties();
			//props.load(is);
			//System.out.println(props.getProperty("java.util.logging.SimpleFormatter.format"));

			LogManager.getLogManager().readConfiguration(is);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void turnOffLogging()
	{
		setLogging();

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
		new SuModule(conf, mode).run();
	}
}
