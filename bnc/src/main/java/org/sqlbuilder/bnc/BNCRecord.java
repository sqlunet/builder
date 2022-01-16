package org.sqlbuilder.bnc;

import org.sqlbuilder.common.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BNCRecord implements Insertable
{
	protected static class IgnoreException extends Exception
	{
		private static final long serialVersionUID = 1L;

		public IgnoreException(String message)
		{
			super(message);
		}
	}

	protected static final Map<String, Character> posMap = new HashMap<>();

	static
	{
		BNCRecord.posMap.put("Adj", 'a');
		BNCRecord.posMap.put("Adv", 'r');
		BNCRecord.posMap.put("Verb", 'v');
		BNCRecord.posMap.put("NoC", 'n');
		// "VMod"
		// "NoP"
		// "NoP-"
		// "ClO",
		// "Conj",
		// "Det",
		// "DetP",
		// "Ex",
		// "Fore",
		// "Gen",
		// "Inf",
		// "Int",
		// "Lett",
		// "Neg",
		// "Num",
		// "Ord",
		// "Prep",
		// "Pron",
		// "Uncl",
	}

	protected static String lastLemma;

	protected static String lastPos;

	protected final String word;

	protected long wordid;

	protected final char pos;

	protected final int freq;

	protected final int range;

	protected final float dispersion;

	protected BNCRecord(final String lemma, final char pos, final int freq, final int range, final float dispersion)
	{
		this.word = lemma;
		this.pos = pos;
		this.freq = freq;
		this.range = range;
		this.dispersion = dispersion;
	}

	public static BNCRecord parse(final String line) throws ParseException, NotFoundException, IgnoreException
	{
		final String[] fields = line.split("\\t+");
		if (fields.length != 7)
		{
			throw new ParseException("Number of fields is not 7");
		}

		// fields
		final String field1 = fields[1];
		final String inflectedForm = fields[3];
		String bncPos = fields[2];

		// expand tab placeholders (do not move: this is a reference to previous line/chain of lines)
		String word = field1;
		if (word.equals("@"))
		{
			word = BNCRecord.lastLemma;
		}
		if (bncPos.equals("@"))
		{
			bncPos = BNCRecord.lastPos;
		}
		BNCRecord.lastLemma = word;
		BNCRecord.lastPos = bncPos;

		// do not process variants
		if (!inflectedForm.equals("%"))
		{
			throw new IgnoreException(inflectedForm);
		}

		// convert data
		final String lemma = makeLemma(word);
		final Character pos = BNCRecord.posMap.get(bncPos);
		if (pos == null)
		{
			throw new NotFoundException(bncPos);
		}

		// freq data
		final int freq = Integer.parseInt(fields[4]);
		final int range = Integer.parseInt(fields[5]);
		final float dispersion = Float.parseFloat(fields[6]);

		return new BNCRecord(lemma, pos, freq, range, dispersion);
	}

	static String makeLemma(String word)
	{
		word = word.trim();
		if (word.endsWith("*"))
		{
			word = word.substring(0, word.length() - 1);
		}
		//TODO
		return word.replace(' ', '_');
	}

	@Override
	public String toString()
	{
		return " [" + word + "," + pos + "] " + freq + " " + range + " " + dispersion;
	}

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%c',%d,%d,%f", wordid, Utils.escape(word), pos, freq, range, dispersion);
	}

	public boolean resolve(final Function<String, Integer> resolver)
	{
		Integer id = resolver.apply(word);
		boolean resolved = id != null;
		wordid = resolved ? id : -1;
		return resolved;
	}
}
