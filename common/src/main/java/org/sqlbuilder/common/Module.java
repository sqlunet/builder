package org.sqlbuilder.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public abstract class Module
{
	public final String id;

	protected final Properties props;

	protected Module(final String id, final String conf)
	{
		this.id = id;
		props = getProperties(conf);
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
}
