/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlbuilder.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Explain
{
	// R E G P A T T E R N S

	private static final String tableName = "([a-z1-2_AS ]+)";
	private static final String indexName = "([a-z1-2_AS ]+)";
	private static final String components = "\\(([^)]+)\\)";

	// private static final Pattern indexPattern1 = Pattern.compile("SEARCH TABLE " + tableName + " USING INDEX");
	// private static final Pattern indexPattern2 = Pattern.compile("SEARCH TABLE " + tableName + " USING INDEX " + indexName);
	private static final Pattern indexPattern3 = Pattern.compile("SEARCH " + tableName + " USING INDEX " + indexName + " " + components);

	// private static final Pattern coveringIndexPattern1 = Pattern.compile("SEARCH TABLE " + tableName + " USING COVERING INDEX");
	// private static final Pattern coveringIndexPattern2 = Pattern.compile("SEARCH TABLE " + tableName + " USING COVERING INDEX " + indexName);
	private static final Pattern coveringIndexPattern3 = Pattern.compile("SEARCH " + tableName + " USING COVERING INDEX " + indexName + " " + components);

	private static final Pattern autoindexPattern = Pattern.compile("SEARCH " + tableName + " USING AUTOMATIC COVERING INDEX " + components);
	private static final Pattern autoindexSubQueryPattern = Pattern.compile("SEARCH (SUBQUERY [0-9]+) USING AUTOMATIC COVERING INDEX " + components);

	private static final Pattern scanPattern = Pattern.compile("SCAN " + tableName);

	// S Q L

	static public String EQP = "EXPLAIN QUERY PLAN ";
	static public String SELECT = "SELECT ";
	static public String FROM = " FROM ";
	static public String WHERE = " WHERE ";

	// C O L L E C T

	static Collection<String> indices = new TreeSet<>();
	static Collection<String> coveringIndices = new TreeSet<>();
	static Collection<String> autoIndices = new TreeSet<>();
	static Collection<String> scans = new TreeSet<>();

	public static void explain(final String[] keys, final Function<String, String[]> qs, final Variables variables, String db) throws InterruptedException, IOException
	{
		for (String key : keys)
		{
			System.out.println("-- " + key);
			String[] aqs = qs.apply(key);

			String table = aqs[0] == null ? null : variables.varSubstitutions(aqs[0]).replace("\"", "");
			String projection = aqs[1] == null ? null : variables.varSubstitutions(aqs[1]).replace("\"", "").replace("{", "").replace("}", "");
			String selection = aqs[2] == null ? null : variables.varSubstitutions(aqs[2]).replace("\"", "");

			if (table != null)
			{
				String q2 = SELECT;
				q2 += projection == null ? "*" : projection;
				q2 += FROM;
				q2 += table;
				if (selection != null)
				{
					q2 += WHERE + selection + ';';
				}
				/*
				else
				{
					q2 += WHERE + "rowid = ? " + ';';
				}
				*/

				System.out.println(q2);

				ProcessBuilder builder = new ProcessBuilder("sqlite3", db, EQP + q2);
				Process p = builder.start();
				String o = getOutput(p);
				String e = getError(p);
				p.waitFor();
				if (!o.isEmpty())
				{
					System.out.println(o.substring(11));
					analyse(o);
				}
				if (!e.isEmpty())
				{
					System.out.println("[E] " + e);
					System.err.println(e);
				}
			}
		}

		System.out.println();
		System.out.println("\nINDICES");
		for (String index : indices)
		{
			System.out.println(index);
		}
		System.out.println("\nCOVERING INDICES");
		for (String index : coveringIndices)
		{
			System.out.println(index);
		}
		System.out.println("\nAUTO INDICES");
		for (String index : autoIndices)
		{
			System.out.println(index);
		}
		System.out.println("\nSCANS");
		for (String scan : scans)
		{
			System.out.println(scan);
		}
	}

	public static void analyse(String s)
	{
		int n = 0;
		for (String line : s.split("\n"))
		{
			String x = extract3(line, indexPattern3);
			if (x != null)
			{
				indices.add(x);
			}
			x = extract3(line, coveringIndexPattern3);
			if (x != null)
			{
				coveringIndices.add(x);
			}
			x = extract2(line, autoindexPattern);
			if (x != null)
			{
				autoIndices.add(x);
			}
			x = extract2(line, autoindexSubQueryPattern);
			if (x != null)
			{
				autoIndices.add(x);
			}
			x = extract(line, scanPattern);
			if (x != null)
			{
				scans.add(x + " " + n);
			}
			n++;
		}
	}

	private static String extract(final String input, final Pattern p)
	{
		Matcher m = p.matcher(input);
		if (m.find())
		{
			return m.group(1).split("\\s")[0];
		}
		return null;
	}

	private static String extract2(final String input, final Pattern p)
	{
		Matcher m = p.matcher(input);
		if (m.find())
		{
			return m.group(1).split("\\s")[0] + " (" + m.group(2) + ")";
		}
		return null;
	}

	private static String extract3(final String input, final Pattern p)
	{
		Matcher m = p.matcher(input);
		if (m.find())
		{
			return m.group(1).split("\\s")[0] + " - " + m.group(2) + " (" + m.group(3) + ")";
		}
		return null;
	}

	private static Function<String, String[]> qFrom(String module)
	{
		switch (module)
		{
			case "wn":
				return new org.sqlunet.wn.Factory();
			case "bnc":
				return new org.sqlunet.bnc.Factory();
			case "sn":
				return new org.sqlunet.sn.Factory();
			case "vn":
				return new org.sqlunet.vn.Factory();
			case "pb":
				return new org.sqlunet.pb.Factory();
			case "fn":
				return new org.sqlunet.fn.Factory();
			case "pm":
				return new org.sqlunet.pm.Factory();
		}
		throw new IllegalArgumentException(module);
	}

	private static String[] keysFrom(String module)
	{
		switch (module)
		{
			case "wn":
				return new org.sqlunet.wn.Factory().get();
			case "bnc":
				return new org.sqlunet.bnc.Factory().get();
			case "sn":
				return new org.sqlunet.sn.Factory().get();
			case "vn":
				return new org.sqlunet.vn.Factory().get();
			case "pb":
				return new org.sqlunet.pb.Factory().get();
			case "fn":
				return new org.sqlunet.fn.Factory().get();
			case "pm":
				return new org.sqlunet.pm.Factory().get();
		}
		throw new IllegalArgumentException(module);
	}

	public static String getOutput(Process p) throws IOException
	{
		StringBuilder result = new StringBuilder();
		try (InputStream is = p.getInputStream(); Scanner s = new Scanner(is).useDelimiter("\\A"))
		{
			while (s.hasNext())
			{
				result.append(s.next());
			}
		}
		return result.toString();
	}

	public static String getError(Process p) throws IOException
	{
		StringBuilder result = new StringBuilder();
		try (InputStream isStream = p.getErrorStream(); Scanner s = new Scanner(isStream).useDelimiter("\\A"))
		{
			while (s.hasNext())
			{
				result.append(s.next());
			}
		}
		return result.toString();
	}

	public static void main(String[] args) throws IOException, InterruptedException
	{
		boolean compat = false;
		if ("-compat".equals(args[0]))
		{
			compat = true;
			args = Arrays.copyOfRange(args, 1, args.length);
		}

		String module = args[0];
		String db = args[1];
		Integer from = null;
		Integer to = null;
		if (args.length >= 4)
		{
			from = Integer.parseInt(args[2]);
			to = Integer.parseInt(args[3]);
		}

		ResourceBundle bundle = ResourceBundle.getBundle(module + "/" + (compat ? "NamesCompat" : "Names"));
		ResourceBundle bundle2 = ResourceBundle.getBundle(module + "/" + (compat ? "NamesCompatExtra" : "NamesExtra"));
		var variables = Variables.make(bundle, bundle2);
		variables.put("suggest_text_1", "suggest_text_1");
		variables.put("suggest_query", "suggest_query");
		variables.put("uri_last", "0");
		variables.put("query", "#{query}"); // to avoid un valued variable

		final String[] allKeys = keysFrom(module);
		final String[] keys = to == null ? allKeys : Arrays.stream(allKeys, from, to).toArray(String[]::new);
		explain(keys, qFrom(module), variables, db);
	}
}
