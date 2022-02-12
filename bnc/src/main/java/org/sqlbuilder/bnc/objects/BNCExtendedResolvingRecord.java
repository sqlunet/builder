package org.sqlbuilder.bnc.objects;

import org.sqlbuilder.common.IgnoreException;
import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.ParseException;
import org.sqlbuilder.common.Utils;

import java.util.function.Function;

public class BNCExtendedResolvingRecord extends BNCResolvingRecord
{
	protected final int freq2;

	protected final int range2;

	protected final float dispersion2;

	protected final float lL;

	// C O N S T R U C T O R

	private BNCExtendedResolvingRecord(final BNCExtendedRecord r)
	{
		super(r.word, r.pos, r.freq, r.range, r.dispersion);
		this.freq2 = r.freq2;
		this.range2 = r.range2;
		this.dispersion2 = r.dispersion2;
		this.lL = r.lL;
	}

	public static BNCExtendedResolvingRecord parse(final String line) throws ParseException, NotFoundException, IgnoreException
	{
		var r = BNCExtendedRecord.parse(line);
		return new BNCExtendedResolvingRecord(r);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%s,'%s','%c',%d,%d,%f,%d,%d,%f,%f", wordid, Utils.escape(word), pos, freq, range, dispersion, freq2, range2, dispersion2, lL);
	}

	// R E S O L V E

	public boolean resolve(final Function<String, Integer> resolver)
	{
		Integer id = resolver.apply(word);
		boolean resolved = id != null;
		wordid = resolved ? id : -1;
		return resolved;
	}
}
