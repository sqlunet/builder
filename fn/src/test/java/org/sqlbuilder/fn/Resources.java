package org.sqlbuilder.fn;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Resources
{
	public static final Resources resources = new Resources();
	static
	{
		final String resourceName = Resources.class.getPackage().getName() + '.' + "normalizer";
		Resources.resources.addResource(ResourceBundle.getBundle(resourceName));
	}

	// key-value map

	private final Map<String, String> resourceMap = new TreeMap<>();

	// initialize

	public void addResource(final ResourceBundle bundle)
	{
		for (final String key : bundle.keySet())
		{
			this.resourceMap.put(key, bundle.getString(key));
		}
	}

	// remove

	public int removeKeys(final String... keys)
	{
		int count = 0;
		for (final String key : keys)
		{
			final String value = this.resourceMap.remove(key);
			if (value != null)
			{
				count++;
				// System.err.println("removed key " + key + " val=" + value);
			}
		}
		return count;
	}

	// concat arrays

	public static String[] concat(final String... strs)
	{
		return strs;
	}

	public static String[] concat(final String[]... strss)
	{
		final List<String> result = new ArrayList<>();
		for (final String[] strs : strss)
		{
			Collections.addAll(result, strs);
		}
		return result.toArray(new String[] {});
	}

	// join

	public static String join(final Collection<String> strs)
	{
		if (strs == null)
			return null;
		final List<String> list = new ArrayList<>(strs);
		Collections.sort(list);
		final StringBuilder sb = new StringBuilder();
		for (final String str : list)
		{
			sb.append(str);
			sb.append(' ');
		}
		return sb.toString();
	}

	// keys

	public List<String> getKeysWithOp(final String str)
	{
		final List<String> result = new ArrayList<>();
		for (final String key : this.resourceMap.keySet())
		{
			final String[] fields = key.split("\\.");
			final String opKey = fields[fields.length - 1];
			if (opKey.startsWith(str))
			{
				result.add(key);
			}
		}
		return result;
	}

	// values

	public List<String> getValuesWithOp(final String str, final boolean strict)
	{
		final List<String> result = new ArrayList<>();
		for (final String key : this.resourceMap.keySet())
		{
			final String[] fields = key.split("\\.");
			final String opKey = fields[fields.length - 1];
			if (strict ? opKey.equals(str) : opKey.startsWith(str))
			{
				result.add(getString(key));
			}
		}
		return result;
	}

	public void dumpAll()
	{
		for (final String key : this.resourceMap.keySet())
		{
			final String value = getString(key);
			if (value.indexOf('!') != -1 || value.indexOf('%') != -1)
			{
				System.out.println(key + '+' + value);
			}
		}
	}

	public void dumpKeys(final String regExp)
	{
		for (final String key : this.resourceMap.keySet())
		{
			if (!key.matches(regExp))
			{
				continue;
			}

			final String value = getString(key);
			if (value.indexOf('!') != -1 || value.indexOf('%') != -1)
			{
				System.out.println(key + "+" + value);
			}
		}
	}

	public void dumpRawKeys(final String regExp)
	{
		for (final Map.Entry<String, String> entry : this.resourceMap.entrySet())
		{
			final String key = entry.getKey();
			final String value = entry.getValue();

			if (!key.matches(regExp))
			{
				continue;
			}

			if (value.indexOf('!') != -1 || value.indexOf('%') != -1)
			{
				System.out.println(key + "+" + value);
			}
		}
	}

	// tables

	public List<String> getTables()
	{
		return getValuesWithOp("table", true);
	}

	public List<String> getTables(final String... tableKeys0)
	{
		if (tableKeys0 == null)
			return getTables();

		final List<String> result = new ArrayList<>();
		for (final String key : getKeysWithOp("table"))
		{
			// add if it matches one of the input table keys
			for (final String tableKey0 : tableKeys0)
			{
				final String[] fields = key.split("\\.");
				final String tableKey = fields[0];
				if (tableKey.equals(tableKey0))
				{
					result.add(getString(key));
				}
			}
		}
		return result;
	}

	// property factory

	public static Properties makeProps(final String... strs)
	{
		final Properties props = new Properties();
		for (int i = 0; i < strs.length; i += 2)
		{
			final String key = strs[i];
			final String value = strs[i + 1];
			props.put(key, value);
		}
		return props;
	}

	// get value

	public String getString(final String key)
	{
		try
		{
			String str = this.resourceMap.get(key);
			if (str == null)
				throw new MissingResourceException(key, Resources.class.getName(), key);

			// System.out.print("\n>[" + key + "] " + str);
			str = expandPercent(str);
			// System.out.println("\n< " + str);
			return str;
		}
		catch (MissingResourceException e)
		{
			System.err.println("NO KEY:" + e.getKey());
			throw e; // '!' + key + '!';
		}
	}

	private String expandPercent(final String str)
	{
		final String REGEX = "%[^%]*%";
		final Pattern pattern = Pattern.compile(REGEX);

		// percents
		int percentCount = 0;
		for (int p = 0; (p = str.indexOf('"', p + 1)) != -1;)
		{
			percentCount++;
		}
		if (percentCount % 2 != 0)
			return null;

		// macro map
		final Map<String, String> map = new HashMap<>();
		final Matcher matcher = pattern.matcher(str); // get a matcher object
		while (matcher.find())
		{
			final String match = matcher.group();
			final String key = match.substring(1, match.length() - 1);
			// System.out.println(" *" + key);
			if (!map.containsKey(key))
			{
				final String value = getString(key);
				map.put(key, value);
			}
		}

		// macro substitution
		String result = str;
		for (final Map.Entry<String, String> entry : map.entrySet())
		{
			final String key = entry.getKey();
			final String value = entry.getValue();
			// String regExp = "%" + key.replace("$", "\\$") + "%";
			// result = result.replaceAll(regExp, value);
			result = result.replace("%" + key + "%", value);
		}
		return result;
	}

	public static String expand(final String str, final String... strs)
	{
		return Resources.expand(str, Resources.makeProps(strs));
	}

	public static String expand(final String str, final Properties props)
	{
		final String REGEX = "\\$[^$]+\\$";
		final Pattern pattern = Pattern.compile(REGEX);

		// match
		String result = str;
		final Matcher matcher = pattern.matcher(str); // get a matcher object
		while (matcher.find())
		{
			final String match = matcher.group();
			final String key = match.substring(1, match.length() - 1);
			final String value = props.getProperty(key);
			if (value == null)
			{
				System.err.println("[" + match + "] -> null");
				throw new NoSuchElementException(match);
			}
			result = result.replaceAll("\\$" + key + "\\$", value);
		}
		return result;
	}
}
