/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlbuilder.common;

import org.sqlunet.vn.QC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
	static public String[] WN_KEYS = Arrays.stream(org.sqlunet.wn.QV.Key.values()).map(Enum::toString).sorted().toArray(String[]::new);

	static public String[] BNC_KEYS = Arrays.stream(org.sqlunet.bnc.QV.Key.values()).map(Enum::toString).toArray(String[]::new);

	static public String[] FN_KEYS = Arrays.stream(org.sqlunet.fn.QV.Key.values()).map(Enum::toString).toArray(String[]::new);

	static public String[] VN_KEYS = Arrays.stream(org.sqlunet.vn.QV.Key.values()).map(Enum::toString).sorted().toArray(String[]::new);

	static public String[] PB_KEYS = Arrays.stream(org.sqlunet.pb.QV.Key.values()).map(Enum::toString).sorted().toArray(String[]::new);
	;
	static public String[] SN_KEYS = Arrays.stream(org.sqlunet.sn.QV.Key.values()).map(Enum::toString).toArray(String[]::new);

	// H E L P E R S

	public static void compare(String[] keys, Q q1, Q q2)
	{
		for (String key : keys)
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

	public static void generateProperties(String[] keys, Q q, PrintStream ps)
	{
		for (String key : keys)
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

	public static void generateClass(final String[] keys, final Q q, final String className, final Runnable runnable, final PrintStream ps)
	{
		ps.println("package provider;\n");
		ps.println("public class " + className + " {\n");

		if (runnable != null)
		{
			ps.println("// VARIABLES");
			runnable.run();
			ps.println();
		}

		for (String key : keys)
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

	private static Q qFrom(String module, String s)
	{
		switch (module)
		{
			case "wn":
				switch (s)
				{
					case "0":
						return new org.sqlunet.wn.Q0();
					case "1":
						return new org.sqlunet.wn.Q1();
					case "2":
						return new org.sqlunet.wn.Q2();
					case "QV":
						return new org.sqlunet.wn.QV();
				}
				return null;
			case "bnc":
				switch (s)
				{
					case "Q0":
						return new org.sqlunet.bnc.Q0();
						/*
					case "Q1":
						return new org.sqlunet.bnc.Q1();
					case "Q2":
						return new org.sqlunet.bnc.Q2();
						*/
					case "QV":
						return new org.sqlunet.bnc.QV();
				}
				return null;
			case "sn":
				switch (s)
				{
					case "Q0":
						return new org.sqlunet.sn.Q0();
					case "QV":
						return new org.sqlunet.sn.QV();
				}
				return null;
			case "vn":
				switch (s)
				{
					case "Q0":
						return new org.sqlunet.vn.Q0();
					case "Q1":
						return new org.sqlunet.vn.Q1();
					case "QC":
						return new QC();
					case "QV":
						return new org.sqlunet.vn.QV();
				}
				return null;
			case "pb":
				switch (s)
				{
					case "0":
						return new org.sqlunet.pb.Q0();
					case "1":
						return new org.sqlunet.pb.Q1();
					case "2":
						return new org.sqlunet.pb.Q2();
					case "QV":
						return new org.sqlunet.pb.QV();
				}
				return null;
			case "fn":
				switch (s)
				{
					case "Q0":
						return new org.sqlunet.fn.Q0();
					case "Q1":
						return new org.sqlunet.fn.Q1();
					case "QV":
						return new org.sqlunet.fn.QV();
				}
				return null;
		}
		return null;
	}

	private static String[] keysFrom(String module)
	{
		switch (module)
		{
			case "wn":
				return WN_KEYS;
			case "bnc":
				return BNC_KEYS;
			case "sn":
				return SN_KEYS;
			case "vn":
				return VN_KEYS;
			case "pb":
				return PB_KEYS;
			case "fn":
				return FN_KEYS;
		}
		return null;
	}

	private static String nullable(String s)
	{
		return s == null ? "" : s;
	}

	public static Variables makeVariables(final String module)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(module + "/" + "Names");
		Variables variables = new Variables(bundle);

		// synonyms
		//variables.put("baserelations.synset2id", variables.varSubstitution("${synsets_synsets.word2id}"));
		//variables.put("baserelations.word2id", variables.varSubstitution("${senses_senses.word2id}"));

		// aliases
		//variables.put("uri_last", "#{uri_last}");
		//variables.put("MAKEQUERY", "#{query}");

		// column aliases
		variables.put("_id", "_id");
		variables.put("name", "name");
		variables.put("members", "members");
		variables.put("members2", "members2");
		variables.put("word2", "word2");
		variables.put("fnid", "fnid");
		variables.put("isframe", "isframe");
		variables.put("src_frame", "sf");
		variables.put("dest_frame", "df");

		// group_concats
		variables.put("sampleset", "sampleset");
		variables.put("annotations", "annotations");

		// table aliases
		variables.put("as_words", "w");
		variables.put("as_words1", "w1");
		variables.put("as_words2", "w2");
		variables.put("as_synsets", "y");
		variables.put("as_synsets1", "y1");
		variables.put("as_synsets2", "y2");
		variables.put("as_senses", "s");
		variables.put("as_relations", "r");
		variables.put("as_types", "t");
		variables.put("as_poses", "p");
		variables.put("as_domains", "d");
		variables.put("as_caseds", "c");
		variables.put("as_members", "m");
		variables.put("as_examples", "e");

		variables.put("as_funcs", "fu");
		variables.put("as_args", "ar");

		variables.put("as_frames", "fr");
		variables.put("as_related_frames", "rf");
		variables.put("as_fes", "fe");
		variables.put("as_fetypes", "ft");
		variables.put("as_lexunits", "lu");
		variables.put("as_sentences", "st");
		variables.put("as_annosets", "an");

		variables.put("dict.table", "dict");

		return variables;
	}

	public static void main(final String[] args) throws IOException
	{
		switch (args[0])
		{
			case "compare":
			{
				String module = args[1];
				String source1 = args[2];
				String source2 = args[3];
				compare(keysFrom(module), qFrom(module, source1), qFrom(module, source2));
				break;
			}

			case "generate_class":
			{
				String module = args[1];
				String source = args[2];
				String className = args[3];
				String fileName = module + "/" + className + ".java";
				try (PrintStream ps = new PrintStream(new FileOutputStream(fileName)))
				{
					generateClass(keysFrom(module), qFrom(module, source), className, () -> makeVariables(args[1]).export(ps), ps);
				}
				break;
			}

			case "generate_properties":
			{
				String module = args[1];
				String source = args[2];
				String propertiesName = args[3];
				String fileName = module + "/" + propertiesName + ".properties";
				try (PrintStream ps = new PrintStream(new FileOutputStream(fileName)))
				{
					generateProperties(keysFrom(module), qFrom(module, source), ps);
				}
				// generateProperties(keysFrom(module), qFrom(module, source), System.out);
				break;
			}

			case "instantiate":
			{
				Variables variables = makeVariables(args[1]);
				String source = args[2];
				String dest = args[3];
				if ("-".equals(dest))
				{
					variables.varSubstitutionInFile(new File(source), System.out, false, false);
				}
				else
				{
					try (PrintStream ps = new PrintStream(new FileOutputStream(dest)))
					{
						variables.varSubstitutionInFile(new File(source), ps, false, false);
					}
				}
				break;
			}

			case "export":
			{
				String module = args[1];
				Variables variables = makeVariables(module);
				variables.export(System.out);
			}
			break;
		}
	}
}
