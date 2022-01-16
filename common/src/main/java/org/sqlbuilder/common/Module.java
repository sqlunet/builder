package org.sqlbuilder.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class Module
{
	public final String id;

	protected final Properties props = new Properties();

	protected Module(final String id, final String conf)
	{
		this.id = id;
		getProperties(conf);
	}

	abstract protected void run();

	private void getProperties(final String conf)
	{
		File confFile = new File(conf);
		try (FileInputStream fis = new FileInputStream(confFile))
		{
			props.load(fis);
		}
		catch (Exception ignored)
		{
		}
	}
}
