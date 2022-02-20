package org.sqlbuilder.bnc.objects;

import org.sqlbuilder.common.*;

import java.util.HashMap;
import java.util.Map;

public class BNCRecord implements Insertable
{
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

	public final String word;

	protected final char pos;

	protected final int freq;

	protected final int range;

	protected final float dispersion;

	// C O N S T R U C T O R

	protected BNCRecord(final String lemma, final char pos, final int freq, final int range, final float dispersion)
	{
		this.word = lemma;
		this.pos = pos;
		this.freq = freq;
		this.range = range;
		this.dispersion = dispersion;
	}

	/*
	1:WORD					2:POS   3:INFLECT       4:FREQ  5:RANGE 6:

	%	                    NoC	    %	            6	    53	    0.64
	@	                    @	    %	            6	    52	    0.64
	@	                    @	    %s	            0	    1	    0.00

	abandon                 NoC     :               1       53      0.87
	abandon                 Verb    %               44      99      0.96
	@                       @       abandon         12      98      0.94
	@                       @       abandoned       26      97      0.96
	@                       @       abandoning      5       90      0.93
	@                       @       abandons        1       47      0.87
	abandoned               Adj     :               4       88      0.92
	abandoned-in-transit    Adj     :               0       1       0.00
	abandoned/ignored       Adj     :               0       1       0.00
	abandonedl              NoC     :               0       1       0.00
	abandonemtn             NoC     :               0       1       0.00
	abandoning              NoC     :               0       12      0.71
	abandonment             NoC     %               5       89      0.92
	@                       @       abandonment     5       89      0.92
	@                       @       abandonments    0       1       0.00
	*/
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

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("'%s','%c',%d,%d,%f", Utils.escape(word), pos, freq, range, dispersion);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return " [" + word + "," + pos + "] " + freq + " " + range + " " + dispersion;
	}
}
