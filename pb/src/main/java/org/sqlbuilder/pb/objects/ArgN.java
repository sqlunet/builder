package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.common.Utils;

import java.util.*;

public class ArgN implements Comparable<ArgN>, Insertable
{
	public static final Comparator<ArgN> COMPARATOR = Comparator.comparing(ArgN::getArgn);

	public static final Set<ArgN> SET = new HashSet<>();

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
		SET.add(a);
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

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		ArgN that = (ArgN) o;
		return argn.equals(that.argn);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(argn);
	}
	// O R D E R

	@Override
	public int compareTo(@NotNull final ArgN that)
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
