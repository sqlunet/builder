package org.sqlbuilder.bnc.objects;

import org.sqlbuilder.common.*;

public class BNCExtendedRecord extends BNCRecord
{
	protected final int freq2;

	protected final int range2;

	protected final float dispersion2;

	protected final float lL;

	protected BNCExtendedRecord(final String lemma, final char pos, final int freq, final int range, final float dispersion, final int freq2, final int range2, final float dispersion2, final float lL)
	{
		super(lemma, pos, freq, range, dispersion);
		this.freq2 = freq2;
		this.range2 = range2;
		this.dispersion2 = dispersion2;
		this.lL = lL;
	}

	public static BNCExtendedRecord parse(final String line) throws ParseException, NotFoundException, IgnoreException
	{
		final String[] fields = line.split("\\t+");
		if (fields.length != 12)
		{
			throw new ParseException("Number of fields is not 12");
		}

		// id
		String word = fields[1];
		final String bncPos = fields[2];
		final String inflectedForm = fields[3];

		// expand tab placeholders (do not move: this is a reference to previous line/chain of lines)
		if (word.equals("@"))
		{
			word = BNCRecord.lastLemma;
		}
		if (word.equals("@"))
		{
			word = BNCRecord.lastPos;
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

		// data
		final int freq = Integer.parseInt(fields[4]);
		final int range = Integer.parseInt(fields[5]);
		final float dispersion = Float.parseFloat(fields[6]);

		// xdata
		final String comp = fields[7];
		float lL = Float.parseFloat(fields[8]) * -1;
		if (comp.equals("-"))
		{
			lL *= -1.f;
		}
		final int freq2 = Integer.parseInt(fields[9]);
		final int range2 = Integer.parseInt(fields[10]);
		final float dispersion2 = Float.parseFloat(fields[11]);

		return new BNCExtendedRecord(lemma, pos, freq, range, dispersion, freq2, range2, dispersion2, lL);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%s,'%s','%c',%d,%d,%f,%d,%d,%f,%f", "NULL", Utils.escape(word), pos, freq, range, dispersion, freq2, range2, dispersion2, lL);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return super.toString() + " " + freq2 + " " + range2 + " " + dispersion2 + " " + lL;
	}
}
