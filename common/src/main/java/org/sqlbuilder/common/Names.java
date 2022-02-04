package org.sqlbuilder.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class Names
{
	private static final Properties props = getProperties("/Names.properties");

	private static Properties getProperties(final String conf)
	{
		Properties props = new Properties();
		URL url = Names.class.getResource(conf);
		assert url != null;
		try (InputStream fis = url.openStream())
		{
			props.load(fis);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
		return props;
	}

	public static String table(String section)
	{
		return get(section + ".table");
	}

	public static String file(String section)
	{
		return get(section + ".file");
	}

	public static String columns(String section)
	{
		return Arrays.stream(get(section + ".columns").split(",")).map(c -> "`" + c + '`').collect(Collectors.joining(","));
	}

	private static String get(String key)
	{
		var v = props.getProperty(key);
		if (v == null)
		{
			throw new IllegalArgumentException(key);
		}
		return v;
	}
}