package org.sqlbuilder.sn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.ParseException;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder2.legacy.Triplet;

import java.util.function.Function;

public class Collocation implements Insertable
{
	public final long offset1;

	public final char pos1;

	public final String word1;

	public final long offset2;

	public final char pos2;

	public final String word2;

	public String sensekey1;

	public String sensekey2;

	// C O N S T R U C T O R

	public static Collocation make(final long offset1, final char pos1, final String lemma1, final long offset2, final char pos2, final String lemma2)
	{
		return new Collocation(offset1, pos1, makeLemma(lemma1), offset2, pos2, makeLemma(lemma2));
	}

	protected Collocation(final long offset1, final char pos1, final String word1, final long offset2, final char pos2, final String word2)
	{
		this.offset1 = offset1;
		this.pos1 = pos1;
		this.word1 = word1;
		this.offset2 = offset2;
		this.pos2 = pos2;
		this.word2 = word2;
	}

	public static Collocation parse(final String line) throws ParseException
	{
		try
		{
			String[] fields = line.split("\\s");

			long synset1Id = Long.parseLong(fields[0].substring(0, fields[0].length() - 1));
			long synset2Id = Long.parseLong(fields[1].substring(0, fields[1].length() - 1));
			String lemma1 = fields[2];
			char pos1 = fields[3].charAt(0);
			String lemma2 = fields[4];
			char pos2 = fields[5].charAt(0);
			return Collocation.make(synset1Id, pos1, lemma1, synset2Id, pos2, lemma2);
		}
		catch (Exception e)
		{
			throw new ParseException(e.getMessage());
		}
	}

	private static String makeLemma(final String word)
	{
		return word.trim().replace('_', ' ');
	}

	// A C C E S S

	public String getSensekey1()
	{
		return sensekey1;
	}

	public String getSensekey2()
	{
		return sensekey2;
	}

	public String getWord1()
	{
		return word1;
	}

	public String getWord2()
	{
		return word2;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		//return String.format("('%s',%d,'%c',%d,%d, '%s',%d,'%c',%d,%d)", Utils.escape(word1), word1id, pos1, offset1, synset1id, Utils.escape(word2), word2id, pos2, offset2, synset2id);
		return String.format("%s,%s", Utils.nullableQuotedEscapedString(sensekey1), Utils.nullableQuotedEscapedString(sensekey2));
	}

	@Override
	public String comment()
	{
		return String.format("%s,%c,%d,%s,%c,%d", word1, pos1, offset1, word2, pos2, offset2);
	}

	// R E S O L V E

	public boolean resolve(final Function<Triplet<String, Character, Long>, String> skResolver)
	{
		String sk1 = skResolver.apply(new Triplet<>(word1, pos1, offset1));
		boolean resolved1 = sk1 != null;
		if (!resolved1)
		{
			System.err.printf("[RK] %s %c %d%n", word1, pos1, offset1);
		}
		if (resolved1)
		{
			sensekey1 = sk1;
		}
		String sk2 = skResolver.apply(new Triplet<>(word2, pos2, offset2));
		boolean resolved2 = sk2 != null;
		if (!resolved2)
		{
			System.err.printf("[RK] %s %c %d%n", word2, pos2, offset2);
		}
		if (resolved2)
		{
			sensekey2 = sk2;
		}
		return resolved1 && resolved2;
	}
}
