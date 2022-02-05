package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.*;

import java.util.Comparator;
import java.util.Locale;
import java.util.Properties;

public class ArgN implements HasId, Comparable<ArgN>, Insertable
{
	public static final Comparator<ArgN> COMPARATOR = Comparator.comparing(ArgN::getArgn);

	public static final SetCollector<ArgN> COLLECTOR = new SetCollector<>(COMPARATOR);

	private static final Properties DESCRIPTIONS = new Properties();

	static
	{
		DESCRIPTIONS.setProperty("M", "modifier");
		DESCRIPTIONS.setProperty("A", "agent");
		DESCRIPTIONS.setProperty("@", "?");
	}

	private final String argn;

	// C O N S T R U C T O R

	public static ArgN make(final String n)
	{
		if (n == null || n.isEmpty())
		{
			return null;
		}
		var a = new ArgN(n);
		COLLECTOR.add(a);
		return a;
	}

	private ArgN(final String n)
	{
		this.argn = n.toUpperCase(Locale.ENGLISH);
	}

	// A C C E S S

	public String getArgn()
	{
		return argn;
	}

	@RequiresIdFrom(type = ArgN.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	@RequiresIdFrom(type = ArgN.class)
	public static Integer getIntId(final ArgN argn)
	{
		return argn == null ? null : COLLECTOR.get(argn);
	}

	// O R D E R

	@Override
	public int compareTo(final ArgN that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("'%s',%s", argn, Utils.nullableQuotedString(DESCRIPTIONS.getProperty(argn, null)));
	}
}
