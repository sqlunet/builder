/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.sqlbuilder.common;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable substitution
 */
public class Variables
{
	/**
	 * Variable-value map
	 */
	public static final Map<String, String> toValue = new HashMap<>();

	/**
	 * Set values in map from resource bundle
	 *
	 * @param bundle resource bundle
	 */
	public static void set(final ResourceBundle bundle)
	{
		for (String k : bundle.keySet())
		{
			toValue.put(k, bundle.getString(k));
		}
	}

	/**
	 * Substitute values to variables in file
	 *
	 * @param file     input file
	 * @param ps       print stream
	 * @param compress whether to compress spaces to single space
	 * @throws IOException io exception
	 */
	public static void varSubstitutionInFile(final File file, final PrintStream ps, final boolean compress) throws IOException
	{
		// iterate on lines
		try (InputStream is = new FileInputStream(file))
		{
			varSubstitutionInIS(is, ps, compress);
		}
		catch (IllegalArgumentException iae)
		{
			System.err.printf("%s %s", file, iae.getMessage());
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
	public static void varSubstitutionInIS(final InputStream is, final PrintStream ps, final boolean compress) throws IOException
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
					line = varSubstitution(line);
				}
				catch (IllegalArgumentException iae)
				{
					System.err.printf("%d '%s'", lineNum, line);
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

	private static boolean USE_BACKTICKS = true;

	private static final Pattern DOLLAR_PATTERN = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)}");

	private static final Pattern AT_PATTERN = Pattern.compile("@\\{([a-zA-Z0-9_.]+)}");

	/**
	 * Substitute values to variables in string
	 *
	 * @param input input string
	 * @return string with values substituted fir variable name
	 */
	public static String varSubstitution(String input)
	{
		return varSubstitution(varSubstitution(input, AT_PATTERN, false), DOLLAR_PATTERN, USE_BACKTICKS);
	}

	/**
	 * Substitute values to variables in string
	 *
	 * @param input        input string
	 * @param p            pattern for variable
	 * @param useBackticks whether to surround substitution result with back ticks
	 * @return string with values substituted for variable name
	 */
	public static String varSubstitution(final String input, final Pattern p, boolean useBackticks)
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
			if (output.contains(p.pattern().substring(0,1)))
			{
				throw new IllegalArgumentException(p.pattern().charAt(0) + ",{,} used in '" + input + "'");
			}
			return output;
		}
		return input;
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
			System.err.printf("%s=%s%n", varName, varName);
		}
	}
}