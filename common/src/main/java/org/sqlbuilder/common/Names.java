package org.sqlbuilder.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class Names
{
	private final Properties props;

	public Names(final String module)
	{
		this.props = getProperties("/" + module + "/Names.properties");
	}

	private Properties getProperties(final String conf)
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

	public String table(String section)
	{
		return get(section + ".table");
	}

	public String file(String section)
	{
		return get(section + ".file");
	}

	public String updateFile(String section)
	{
		return "update_" + get(section + ".file");
	}

	public String columns(String section)
	{
		return columns(section, false);
	}

	public String columns(String section, boolean resolve)
	{
		return backtickColumns(get(section + (resolve ? ".columns.resolved" : ".columns")));
	}

	public String get(String key)
	{
		var v = props.getProperty(key);
		if (v == null)
		{
			throw new IllegalArgumentException(key);
		}
		return v;
	}

	private String backtickColumns(final String columns)
	{
		return Arrays.stream(columns.split(",")).map(c -> "`" + c + '`').collect(Collectors.joining(","));
	}
}