/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.sqlbuilder.common;

import org.sqlbuilder2.ser.Pair;

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
	private static final Pattern DOLLAR_PATTERN = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)}");

	private static final Pattern AT_PATTERN = Pattern.compile("@\\{([a-zA-Z0-9_.]+)}");

	/**
	 * Variable-value map
	 */
	public final Map<String, String> toValue = new HashMap<>();

	/**
	 * Set values in map from resource bundle
	 *
	 * @param bundle resource bundle
	 */
	public Variables(final ResourceBundle bundle)
	{
		for (String k : bundle.keySet())
		{
			toValue.put(k, bundle.getString(k));
		}
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
					})
					.sorted()
					.distinct()
					.forEach(consumer);
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
		toValue.keySet().stream().map(k -> new Pair<>(k, k.contains(".") ?
				k.substring(k.lastIndexOf('.') + 1) :
				k)).filter(kk -> !Set.of("table", "file", "columns", "resolved").contains(kk.getSecond())).sorted(Comparator.comparing(Pair<String, String>::getSecond)).map(kk -> String.format("public static final String %s=\"%s\";", kk.getSecond().toUpperCase(Locale.ROOT), toValue.get(kk.getFirst()))).distinct().forEach(ps::println);
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
			if (output.contains(p.pattern().substring(0, 1)))
			{
				throw new IllegalArgumentException(p.pattern().charAt(0) + ",{,} used in '" + input + "'");
			}
			return output;
		}
		return input;
	}
}
