package org.sqlbuilder.sl.foreign;

import org.sqlbuilder.common.NotNull;

import java.util.Comparator;
import java.util.Objects;

public class PbRole implements Comparable<PbRole>
{
	static final Comparator<PbRole> COMPARATOR = Comparator.comparing(PbRole::getRoleSet).thenComparing(PbRole::getArg);

	public final String roleSet;

	public final String arg;

	// C O N S T R U C T O R

	public static PbRole make(final String roleSet, final String arg)
	{
		return new PbRole(roleSet, arg);
	}

	private PbRole(final String roleSet, final String arg)
	{
		this.roleSet = roleSet;
		this.arg = arg;
	}

	// A C C E S S

	public String getRoleSet()
	{
		return roleSet;
	}

	public String getArg()
	{
		return arg;
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
		PbRole that = (PbRole) o;
		return roleSet.equals(that.roleSet) && arg.equals(that.arg);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(roleSet, arg);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final PbRole that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("(%s,%s)", roleSet, arg);
	}
}
