/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.sqlbuilder.common;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Variable substitution
 */
public class Variables
{
	public static final Pattern NUMBER_PATTERN = Pattern.compile("#\\{([a-zA-Z0-9_.]+)}");
	public static final Pattern DOLLAR_PATTERN = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)}");
	public static final Pattern AT_PATTERN = Pattern.compile("@\\{([a-zA-Z0-9_.]+)}");

	/**
	 * Name values pairs
	 */
	public final Map<String, String> toValue = new HashMap<>();

	/**
	 * Constructor
	 */
	private Variables()
	{
	}

	/**
	 * Set values in map from properties
	 *
	 * @param propertiesPaths resource bundle
	 * @return variables
	 */
	public static Variables make(final String... propertiesPaths) throws IOException
	{
		var v = new Variables();
		Properties properties = new Properties();
		for (var propertiesPath : propertiesPaths)
		{
			try (FileInputStream fis = new FileInputStream(propertiesPath))
			{
				properties.load(fis);
			}
		}
		for (var o : properties.keySet())
		{
			String k = o.toString();
			v.toValue.put(k, properties.getProperty(k));
		}
		return v;
	}

	/**
	 * Set values in map from resource bundles
	 *
	 * @param bundles resource bundles
	 * @return variables
	 */
	public static Variables make(final ResourceBundle... bundles)
	{
		var v = new Variables();
		for (ResourceBundle bundle : bundles)
		{
			for (String k : bundle.keySet())
			{
				v.toValue.put(k, bundle.getString(k));
			}
		}
		return v;
	}

	/**
	 * Scan input and produces list on stderr with same value
	 *
	 * @param input input
	 */
	public static void dumpVars(File input, Consumer<String> consumer) throws IOException
	{
		Pattern p = Pattern.compile("[$@]\\{([a-zA-Z0-9_.]+)}");
		try (Stream<String> stream = Files.lines(input.toPath()))
		{
			stream //
					.flatMap(line -> {
						List<String> vars = new ArrayList<>();
						Matcher m = p.matcher(line);
						while (m.find())
						{
							String varName = m.group(1);
							vars.add(varName);
						}
						return vars.stream();
					}).sorted().distinct().forEach(consumer);
		}
	}

	/**
	 * Scan input and produces list on stderr with same value
	 *
	 * @param input input
	 */
	public static void dumpVars(String input)
	{
		Pattern p = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)}");
		Matcher m = p.matcher(input);
		if (m.find())
		{
			String varName = m.group(1);
			System.err.printf("%s%n", varName);
		}
	}

	/**
	 * Export key-value pairs to print stream
	 */
	public void dumpVals(final PrintStream ps)
	{
		toValue.entrySet().forEach(ps::println);
	}

	/**
	 * Export key-value pairs to print stream
	 */
	public void export(final PrintStream ps)
	{
		toValue.keySet().stream().map(k -> new AbstractMap.SimpleEntry<>(k, k.contains(".") ? k.substring(k.lastIndexOf('.') + 1) : k)) //
				.filter(kk -> !Set.of("table", "file", "columns", "resolved").contains(kk.getValue())) //
				.sorted(Comparator.comparing(Map.Entry<String, String>::getValue)) //
				.map(e -> String.format("public static final String %s=\"%s\";", e.getValue().toUpperCase(Locale.ROOT), toValue.get(e.getKey()))) //
				.distinct() //
				.forEach(ps::println);
	}

	/**
	 * Add key-value pair
	 *
	 * @param key   key
	 * @param value value
	 * @return old value if present, null otherwise
	 */
	public String put(final String key, final String value)
	{
		return toValue.put(key, value);
	}

	/**
	 * Substitute values to variables in file
	 *
	 * @param file     input file
	 * @param ps       print stream
	 * @param compress whether to compress spaces to single space
	 * @throws IOException io exception
	 */
	public void varSubstitutionInFile(final File file, final PrintStream ps, boolean useBackticks, final boolean compress) throws IOException
	{
		// iterate on lines
		try (InputStream is = new FileInputStream(file))
		{
			varSubstitutionInIS(is, ps, useBackticks, compress);
		}
		catch (IllegalArgumentException iae)
		{
			System.err.printf("At %s%n%s%n", file, iae.getMessage());
			throw iae;
		}
	}

	/**
	 * Substitute values to variables in input stream
	 *
	 * @param is       input stream
	 * @param ps       print stream
	 * @param compress whether to compress spaces to single space
	 * @throws IOException io exception
	 */
	public void varSubstitutionInIS(final InputStream is, final PrintStream ps, boolean useBackticks, final boolean compress) throws IOException
	{
		// iterate on lines
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset())))
		{
			int lineNum = 0;
			String line;
			while ((line = reader.readLine()) != null)
			{
				lineNum++;
				try
				{
					//initVars(line);
					line = varSubstitution(line, useBackticks);
				}
				catch (IllegalArgumentException iae)
				{
					System.err.printf("At line %d content: [%s]%n", lineNum, line);
					throw iae;
				}
				if (compress)
				{
					line = line.replaceAll("\\s+", " ");
				}
				ps.println(line);
			}
		}
	}

	/**
	 * Substitute values to variables in string
	 *
	 * @param input input string
	 * @return string with values substituted fir variable name
	 */
	public String varSubstitution(String input)
	{
		return varSubstitution(varSubstitution(input, AT_PATTERN, false), DOLLAR_PATTERN, false);
	}

	/**
	 * Substitute values to variables in string
	 *
	 * @param input        input string
	 * @param useBackticks whether to surround substitution result with back ticks
	 * @return string with values substituted fir variable name
	 */
	public String varSubstitution(String input, boolean useBackticks)
	{
		return varSubstitution(varSubstitution(input, AT_PATTERN, false), DOLLAR_PATTERN, useBackticks);
	}

	/**
	 * Substitute values to variables in string
	 *
	 * @param input        input string
	 * @param p            pattern for variable
	 * @param useBackticks whether to surround substitution result with back ticks
	 * @return string with values substituted for variable name
	 */
	public String varSubstitution(final String input, final Pattern p, boolean useBackticks)
	{
		Matcher m = p.matcher(input);
		if (m.find())
		{
			var output = m.replaceAll(r -> {
				String varName = r.group(1);
				if (!toValue.containsKey(varName))
				{
					throw new IllegalArgumentException(varName);
				}
				var val = toValue.get(varName);
				return useBackticks ? "`" + val + '`' : val;
			});
			return output;
		}
		return input;
	}

	/**
	 * Substitute values to variables in string
	 *
	 * @param input0 input string
	 * @param ps     patterns for variable
	 * @return string with values substituted for variable name
	 */
	public String varSubstitutions(final String input0, final Pattern... ps)
	{
		String input = input0;
		for (Pattern p : ps.length == 0 ? new Pattern[]{DOLLAR_PATTERN, AT_PATTERN, NUMBER_PATTERN} : ps)
		{
			input = varSubstitution(input, p, false);
		}
		return input;
	}
}
