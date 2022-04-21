package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.common.Utils;

import java.util.*;

public class ArgType implements Comparable<ArgType>, Insertable
{
	public static final Comparator<ArgType> COMPARATOR = Comparator.comparing(ArgType::getArgType);

	public static final Set<ArgType> SET = new HashSet<>();

	private static final Properties DESCRIPTIONS = new Properties();

	static
	{
		DESCRIPTIONS.setProperty("0", "[0]");
		DESCRIPTIONS.setProperty("1", "[1]");
		DESCRIPTIONS.setProperty("2", "[2]");
		DESCRIPTIONS.setProperty("3", "[3]");
		DESCRIPTIONS.setProperty("4", "[4]");
		DESCRIPTIONS.setProperty("5", "[5]");
		DESCRIPTIONS.setProperty("6", "[6]");
		DESCRIPTIONS.setProperty("M", "modifier");
		DESCRIPTIONS.setProperty("A", "agent");
		DESCRIPTIONS.setProperty("@", "?");
	}

	private final String argType;

	// C O N S T R U C T O R

	public static ArgType make(final String n)
	{
		if (n == null || n.isEmpty())
		{
			return null;
		}
		var a = new ArgType(n);
		SET.add(a);
		return a;
	}

	private ArgType(final String n)
	{
		this.argType = n.toUpperCase(Locale.ENGLISH);
	}

	// A C C E S S

	public String getArgType()
	{
		return argType;
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
		ArgType that = (ArgType) o;
		return argType.equals(that.argType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(argType);
	}
	// O R D E R

	@Override
	public int compareTo(@NotNull final ArgType that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("'%s',%s", argType, Utils.nullableQuotedString(DESCRIPTIONS.getProperty(argType, null)));
	}
}
