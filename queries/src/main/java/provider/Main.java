/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package provider;

import org.sqlbuilder.common.Variables;
import org.sqlbuilder.common.Variables2;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * WordNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Main
{
	static public String[] KEYS = {"WORDS", "WORD", /*"WORD_BY_LEMMA",*/ "SENSES", "SENSE", "SYNSETS", "SYNSET", "SEMRELATIONS", "LEXRELATIONS", "RELATIONS", "POSES", "DOMAINS", "ADJPOSITIONS", "SAMPLES",

			"DICT",

			"WORDS_SENSES_SYNSETS", "WORDS_SENSES_CASEDWORDS_SYNSETS", "WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS", "SENSES_WORDS", "SENSES_WORDS_BY_SYNSET", "SENSES_SYNSETS_POSES_DOMAINS", "SYNSETS_POSES_DOMAINS",

			"BASERELATIONS_SENSES_WORDS_X_BY_SYNSET", "SEMRELATIONS_SYNSETS", "SEMRELATIONS_SYNSETS_X", "SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET", "LEXRELATIONS_SENSES", "LEXRELATIONS_SENSES_X", "LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET",

			"SENSES_VFRAMES", "SENSES_VTEMPLATES", "SENSES_ADJPOSITIONS", "LEXES_MORPHS", "WORDS_LEXES_MORPHS", "WORDS_LEXES_MORPHS_BY_WORD",

			"LOOKUP_FTS_WORDS", "LOOKUP_FTS_DEFINITIONS", "LOOKUP_FTS_SAMPLES",

			"SUGGEST_WORDS", "SUGGEST_FTS_WORDS", "SUGGEST_FTS_DEFINITIONS", "SUGGEST_FTS_SAMPLES",};

	// H E L P E R S

	/**
	 * Append items to projection
	 *
	 * @param projection original projection
	 * @param items      items to addItem to projection
	 * @return augmented projection
	 */
	static protected String[] appendProjection(final String[] projection, final String... items)
	{
		String[] projection2;
		int i = 0;
		if (projection == null)
		{
			projection2 = new String[1 + items.length];
			projection2[i++] = "*";
		}
		else
		{
			projection2 = new String[projection.length + items.length];
			for (final String item : projection)
			{
				projection2[i++] = item;
			}
		}

		for (final String item : items)
		{
			projection2[i++] = item;
		}
		return projection2;
	}

	/**
	 * Add items to projection
	 *
	 * @param projection original projection
	 * @param items      items to addItem to projection
	 * @return augmented projection
	 */
	static String[] prependProjection(final String[] projection, final String... items)
	{
		String[] projection2;
		if (projection == null)
		{
			projection2 = new String[1 + items.length];
		}
		else
		{
			projection2 = new String[projection.length + items.length];
		}
		int i = 0;
		for (final String item : items)
		{
			projection2[i++] = item;
		}
		if (projection == null)
		{
			projection2[i] = "*";
		}
		else
		{
			for (final String item : projection)
			{
				projection2[i++] = item;
			}
		}
		return projection2;
	}

	public static void compare(Q q1, Q q2)
	{
		for (String key : KEYS)
		{
			String[] result1 = q1.query(key);
			String[] result2 = q2.query(key);
			if (!Arrays.equals(result1, result2))
			{
				System.out.println(key);
				if (!Objects.equals(result1[0], result2[0]))
				{
					System.out.println(result1[0] + "\n" + result2[0]);
				}
				if (!Objects.equals(result1[1], result2[1]))
				{
					System.out.println(result1[1] + "\n" + result2[1]);
				}
				if (!Objects.equals(result1[2], result2[2]))
				{
					System.out.println(result1[2] + "\n" + result2[2]);
				}
				if (!Objects.equals(result1[3], result2[3]))
				{
					System.out.println(result1[3] + "\n" + result2[3]);
				}
				if (!Objects.equals(result1[4], result2[4]))
				{
					System.out.println(result1[4] + "\n" + result2[4]);
				}
				System.out.println();
			}
		}
	}

	private static String nullable(String s)
	{
		return s == null ? "" : s;
	}

	public static void generateProperties(Q q, PrintStream ps)
	{
		for (String key : KEYS)
		{
			String[] result = q.query(key);
			ps.println("# " + key);
			ps.println(key + ".t=" + nullable(result[0]));
			ps.println(key + ".p=" + nullable(result[1]));
			ps.println(key + ".s=" + nullable(result[2]));
			ps.println(key + ".a=" + nullable(result[3]));
			ps.println(key + ".g=" + nullable(result[4]));
			ps.println();
		}
	}

	public static void generateClass(Q q, String className, PrintStream ps)
	{
		ps.println("package provider;\n");
		ps.println("public class " + className + " {\n");
		for (String key : KEYS)
		{
			ps.printf("static public class %s {%n", key);
			String[] result = q.query(key);

			ps.printf("\tstatic public final String TABLE = %s;%n", result[0]);
			if (result[1] != null)
			{
				ps.printf("\tstatic public final String[] PROJECTION = %s;%n", result[1]);
			}
			if (result[2] != null)
			{
				ps.printf("\tstatic public final String SELECTION = %s;%n", result[2]);
			}
			if (result[3] != null)
			{
				ps.printf("\tstatic public final String[] ARGS = %s;%n", result[3]);
			}
			if (result[4] != null)
			{
				ps.printf("\tstatic public final String GROUPBY = %s;%n", result[4]);
			}
			ps.println("}");
			ps.println();
		}
		ps.println("}");
	}

	private static Q from(String s)
	{
		switch (s)
		{
			case "0":
				return new Q0();
			case "1":
				return new Q1();
			case "2":
				return new Q2();
			case "V":
				return new QV();
		}
		return null;
	}

	public static void main(final String[] args) throws IOException
	{
		if (args.length == 1)
		{
			String className = "WnQueries";
			String fileName = "wn/" + className + ".java";
			try (PrintStream ps = new PrintStream(new FileOutputStream(fileName)))
			{
				generateClass(from(args[0]), className, ps);
			}
			try (PrintStream ps = new PrintStream(new FileOutputStream("wn/wn.properties")))
			{
				generateProperties(from(args[0]), ps);
			}
			// generateProperties(from(args[0]), System.out);

			String module = "wn";
			ResourceBundle bundle = ResourceBundle.getBundle(module + "/" + "Names");
			Variables2 variables = new Variables2(bundle);
			try (PrintStream ps = new PrintStream(new FileOutputStream("wn/" + className + "Instance.java")))
			{
				variables.varSubstitutionInFile(new File(fileName), ps, false);
			}
		}
		else if (args.length > 1)
		{
			compare(from(args[0]), from(args[1]));
		}
	}
}
