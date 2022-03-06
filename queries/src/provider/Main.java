/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package provider;

import java.util.Arrays;
import java.util.Objects;

/**
 * WordNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Main
{
	// table codes
	static public final int WORDS = 10;
	static public final int WORD = 11;
	static public final int WORD_BY_LEMMA = 12;
	static public final int SENSES = 20;
	static public final int SENSE = 21;
	static public final int SYNSETS = 30;
	static public final int SYNSET = 31;
	static public final int SEMRELATIONS = 40;
	static public final int LEXRELATIONS = 50;
	static public final int RELATIONS = 60;
	static public final int POSES = 70;
	static public final int DOMAINS = 80;
	static public final int ADJPOSITIONS = 90;
	static public final int SAMPLES = 100;

	// view codes
	static public final int DICT = 200;

	// join codes
	static public final int WORDS_SENSES_SYNSETS = 310;
	static public final int WORDS_SENSES_CASEDWORDS_SYNSETS = 311;
	static public final int WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS = 312;
	static public final int SENSES_WORDS = 320;
	static public final int SENSES_WORDS_BY_SYNSET = 321;
	static public final int SENSES_SYNSETS_POSES_DOMAINS = 330;
	static public final int SYNSETS_POSES_DOMAINS = 340;

	static public final int BASERELATIONS_SENSES_WORDS_X_BY_SYNSET = 400;
	static public final int SEMRELATIONS_SYNSETS = 410;
	static public final int SEMRELATIONS_SYNSETS_X = 411;
	static public final int SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET = 412;
	static public final int LEXRELATIONS_SENSES = 420;
	static public final int LEXRELATIONS_SENSES_X = 421;
	static public final int LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET = 422;

	static public final int SENSES_VFRAMES = 510;
	static public final int SENSES_VTEMPLATES = 515;
	static public final int SENSES_ADJPOSITIONS = 520;
	static public final int LEXES_MORPHS = 530;
	static public final int WORDS_LEXES_MORPHS = 541;
	static public final int WORDS_LEXES_MORPHS_BY_WORD = 542;

	// search text codes
	static public final int LOOKUP_FTS_WORDS = 810;
	static public final int LOOKUP_FTS_DEFINITIONS = 820;
	static public final int LOOKUP_FTS_SAMPLES = 830;

	// suggest codes
	static public final int SUGGEST_WORDS = 900;
	static public final int SUGGEST_FTS_WORDS = 910;
	static public final int SUGGEST_FTS_DEFINITIONS = 920;
	static public final int SUGGEST_FTS_SAMPLES = 930;

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
		for (int i = 0; i < 1000; i++)
		{
			String[] result1 = q1.query(i);
			String[] result2 = q2.query(i);
			if (result1 == null && result2 == null)
			{
				continue;
			}
			if (!Arrays.equals(result1, result2))
			{
				System.out.println(i);
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

	public static void gen(Q q)
	{
		for (int i = 0; i < 1000; i++)
		{
			String[] result = q.query(i);
			if (result != null)
			{
				System.out.println(i);
				System.out.println("T:" + result[0]);
				System.out.println("P:" + result[1]);
				System.out.println("S:" + result[2]);
				System.out.println("A:" + result[3]);
				System.out.println("G:" + result[4]);
				System.out.println();
			}
		}
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
		}
		return null;
	}

	public static void main(final String[] args)
	{
		if (args.length == 1)
		{
			gen(from(args[0]));
		}
		else if (args.length > 1)
		{
			compare(from(args[0]), from(args[1]));
		}
	}
}
