package org.sqlbuilder.bnc.objects;

import org.sqlbuilder.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BNCResolvingRecord extends BNCRecord implements Insertable
{
	protected int wordid;

	// C O N S T R U C T O R

	public BNCResolvingRecord(final BNCRecord r)
	{
		super(r.word, r.pos, r.freq, r.range, r.dispersion);
	}

	public static BNCResolvingRecord parse(final String line) throws ParseException, NotFoundException, IgnoreException
	{
		var r = BNCRecord.parse(line);
		return new BNCResolvingRecord(r);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%c',%d,%d,%f", wordid, Utils.escape(word), pos, freq, range, dispersion);
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
