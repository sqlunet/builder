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
	static public String[] WN_KEYS = {"WORDS", "WORD", /*"WORD_BY_LEMMA",*/ "SENSES", "SENSE", "SYNSETS", "SYNSET", "SEMRELATIONS", "LEXRELATIONS", "RELATIONS", "POSES", "DOMAINS", "ADJPOSITIONS", "SAMPLES", "DICT", "WORDS_SENSES_SYNSETS", "WORDS_SENSES_CASEDWORDS_SYNSETS", "WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS", "SENSES_WORDS", "SENSES_WORDS_BY_SYNSET", "SENSES_SYNSETS_POSES_DOMAINS", "SYNSETS_POSES_DOMAINS", "BASERELATIONS_SENSES_WORDS_X_BY_SYNSET", "SEMRELATIONS_SYNSETS", "SEMRELATIONS_SYNSETS_X", "SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET", "LEXRELATIONS_SENSES", "LEXRELATIONS_SENSES_X", "LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET", "SENSES_VFRAMES", "SENSES_VTEMPLATES", "SENSES_ADJPOSITIONS", "LEXES_MORPHS", "WORDS_LEXES_MORPHS", "WORDS_LEXES_MORPHS_BY_WORD", "LOOKUP_FTS_WORDS", "LOOKUP_FTS_DEFINITIONS", "LOOKUP_FTS_SAMPLES", "SUGGEST_WORDS", "SUGGEST_FTS_WORDS", "SUGGEST_FTS_DEFINITIONS", "SUGGEST_FTS_SAMPLES",};

	static public String[] BNC_KEYS = {"BNCS", "WORDS_BNCS"};

	static public String[] FN_KEYS = {"LEXUNITS", "FRAMES", "ANNOSETS", "SENTENCES", "LEXUNIT", "FRAME", "SENTENCE", "ANNOSET", "LEXUNITS_OR_FRAMES", "FRAMES_X_BY_FRAME", "FRAMES_RELATED", "LEXUNITS_X_BY_LEXUNIT", "SENTENCES_LAYERS_X", "ANNOSETS_LAYERS_X", "PATTERNS_LAYERS_X", "VALENCEUNITS_LAYERS_X", "WORDS_LEXUNITS_FRAMES", "FRAMES_FES_BY_FE", "FRAMES_FES", "LEXUNITS_SENTENCES_BY_SENTENCE", "LEXUNITS_SENTENCES", "LEXUNITS_SENTENCES_ANNOSETS_LAYERS_LABELS_BY_SENTENCE", "LEXUNITS_SENTENCES_ANNOSETS_LAYERS_LABELS", "LEXUNITS_GOVERNORS", "GOVERNORS_ANNOSETS", "LEXUNITS_REALIZATIONS_BY_REALIZATION", "LEXUNITS_REALIZATIONS", "LEXUNITS_GROUPREALIZATIONS_BY_PATTERN", "LEXUNITS_GROUPREALIZATIONS", "PATTERNS_SENTENCES", "VALENCEUNITS_SENTENCES", "LOOKUP_FTS_WORDS", "LOOKUP_FTS_SENTENCES", "LOOKUP_FTS_SENTENCES_X_BY_SENTENCE", "LOOKUP_FTS_SENTENCES_X", "SUGGEST_WORDS", "SUGGEST_FTS_WORDS"};

	static public String[] VN_KEYS = {"VNCLASS", "VNCLASSES_X_BY_VNCLASS", "WORDS_VNCLASSES", "VNCLASSES_VNMEMBERS_X_BY_WORD", "VNCLASSES_VNROLES_X_BY_VNROLE", "VNCLASSES_VNFRAMES_X_BY_VNFRAME", "LOOKUP_FTS_EXAMPLES", "LOOKUP_FTS_EXAMPLES_X_BY_EXAMPLE", "LOOKUP_FTS_EXAMPLES_X", "SUGGEST_WORDS", "SUGGEST_FTS_WORDS"};

	static public String[] PB_KEYS = {"PBROLESET", "PBROLESETS", "PBROLESETS_X_BY_ROLESET", "PBROLESETS_X", "WORDS_PBROLESETS", "PBROLESETS_PBROLES", "PBROLESETS_PBEXAMPLES_BY_EXAMPLE", "PBROLESETS_PBEXAMPLES", "LOOKUP_FTS_EXAMPLES", "SUGGEST_WORDS", "SUGGEST_FTS_WORDS"};

	static public String[] SN_KEYS = {"COLLOCATIONS", "COLLOCATIONS_X"};

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

	public static void generateClass(String[] keys, Q q, String className, PrintStream ps)
	{
		ps.println("package provider;\n");
		ps.println("public class " + className + " {\n");
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
					case "V":
						return new org.sqlunet.wn.QV();
				}
				return null;
			case "bnc":
				switch (s)
				{
					case "0":
						return new org.sqlunet.bnc.Q0();
						/*
					case "1":
						return new org.sqlunet.bnc.Q1();
					case "2":
						return new org.sqlunet.bnc.Q2();
						*/
					case "V":
						return new org.sqlunet.bnc.QV();
				}
				return null;
			case "sn":
				switch (s)
				{
					case "0":
						return new org.sqlunet.sn.Q0();
					//case "V":
					//	return new org.sqlunet.sn.QV();
				}
				return null;
			case "vn":
				switch (s)
				{
					case "0":
						return new org.sqlunet.vn.Q0();
					case "1":
						return new org.sqlunet.vn.Q1();
					case "2":
						return new QC();
					case "V":
						return new org.sqlunet.vn.QV();
				}
				return null;
			case "pb":
				switch (s)
				{
					case "0":
						return new org.sqlunet.pb.Q0();
					//case "V":
					//	return new org.sqlunet.pb.QV();
				}
				return null;
			case "fn":
				switch (s)
				{
					case "0":
						return new org.sqlunet.fn.Q0();
					//case "V":
					//	return new org.sqlunet.fn.QV();
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
		variables.put("members", "members");
		variables.put("members2", "members2");
		variables.put("word2", "word2");
		variables.put("sampleset", "sampleset");
		variables.put("d_synsetid", "d_synsetid");
		variables.put("d_wordid", "d_wordid");
		variables.put("d_word", "#d_word");
		variables.put("uri_last", "#{uri_last}");

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
					generateClass(keysFrom(module), qFrom(module, source), className, ps);
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
					variables.varSubstitutionInFile(new File(source), System.out, false);
				}
				else
				{
					try (PrintStream ps = new PrintStream(new FileOutputStream(dest)))
					{
						variables.varSubstitutionInFile(new File(source), ps, false);
					}
				}
				break;
			}
		}
	}
}
