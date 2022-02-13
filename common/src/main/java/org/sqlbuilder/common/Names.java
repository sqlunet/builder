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

	public String table(String key)
	{
		return Utils.backtick(get(key + ".table"));
	}

	public String file(String key)
	{
		return get(key + ".file");
	}

	public String updateFile(String key)
	{
		return "update_" + get(key + ".file");
	}

	public String columns(String key)
	{
		return columns(key, false);
	}

	public String columns(String key, boolean resolve)
	{
		return backtickColumns(get(key + (resolve ? ".columns.resolved" : ".columns")));
	}

	public String column(String key)
	{
		return Utils.backtick(get(key));
	}

	private String get(String key)
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
		return Arrays.stream(columns.split(",")).map(Utils::backtick).collect(Collectors.joining(","));
	}
}