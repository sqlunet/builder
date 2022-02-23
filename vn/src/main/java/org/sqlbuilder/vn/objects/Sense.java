package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Resolvable;
import org.sqlbuilder.common.Utils;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Sense implements Insertable, Resolvable<String, SimpleEntry<Integer, Integer>>, Comparable<Sense>
{
	static public final Comparator<Sense> COMPARATOR = Comparator //
			.comparing(Sense::getSensekey, Comparator.nullsFirst(Comparator.naturalOrder()));

	public static final Set<Sense> SET = new HashSet<>();

	private final Sensekey sensekey;

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static Sense make(final Sensekey sensekey)
	{
		var m = new Sense(sensekey);
		SET.add(m);
		return m;
	}

	private Sense(final Sensekey sensekey)
	{
		this.sensekey = sensekey;
	}

	// A C C E S S

	public Sensekey getSensekey()
	{
		return sensekey;
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Sense that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%s", Utils.nullableQuotedString(sensekey, Sensekey::getSensekey));
	}

	// R E S O L V E

	@Override
	public String resolving()
	{
		return sensekey == null ? null : sensekey.getSensekey();
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s", sensekey);
	}
}
