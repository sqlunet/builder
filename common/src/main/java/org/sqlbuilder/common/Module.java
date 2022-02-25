package org.sqlbuilder.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public abstract class Module
{
	public enum Mode
	{
		PLAIN, RESOLVE, UPDATE, EXPORT;

		public static Mode read(final String arg)
		{
			switch (arg)
			{
				case "-resolve":
					return Mode.RESOLVE;
				case "-update":
					return Mode.UPDATE;
				case "-export":
					return Mode.EXPORT;
				default:
					return Mode.PLAIN;
			}
		}
	}

	public final String id;

	protected final Properties props;

	protected final Mode mode;

	protected Module(final String id, final String conf, final Mode mode)
	{
		this.id = id;
		this.mode = mode;
		this.props = getProperties(conf);
	}

	abstract protected void run();

	@Nullable
	public static Properties getProperties(final String conf)
	{
		File confFile = new File(conf);
		try (FileInputStream fis = new FileInputStream(confFile))
		{
			Properties props = new Properties();
			props.load(fis);
			return props;
		}
		catch (Exception ignored)
		{
		}
		return null;
	}

	public String getId()
	{
		return id;
	}
}
