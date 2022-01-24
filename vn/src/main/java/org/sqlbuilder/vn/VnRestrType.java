package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class VnRestrType implements Insertable<VnRestrType>, Comparable<VnRestrType>
{
	protected static final Set<VnRestrType> SET = new HashSet<>();

	public static Map<VnRestrType, Integer> MAP;

	private final String value;

	private final String type;

	final boolean isSyntactic;

	// C O N S T R U C T

	public VnRestrType(final String value, final String type, final boolean isSyntactic0)
	{
		this.value = value;
		this.type = type;
		this.isSyntactic = isSyntactic0;
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
		VnRestrType that = (VnRestrType) o;
		return isSyntactic == that.isSyntactic && value.equals(that.value) && type.equals(that.type);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(value, type, isSyntactic);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnRestrType that)
	{
		final int cmp = this.type.compareTo(that.type);
		if (cmp != 0)
		{
			return cmp;
		}
		if (this.isSyntactic == that.isSyntactic)
		// same syntactic/selection flag
		{
			return this.value.compareTo(that.value);
		}
		return Boolean.compare(this.isSyntactic, that.isSyntactic);
	}

	// A C C E S S

	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder();
		buffer.append(this.value);
		buffer.append(this.type);
		if (this.isSyntactic)
		{
			buffer.append('*');
		}
		return buffer.toString();
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// id
		// value
		// type
		// isSyntactic
		return String.format("'%s','%s',%b", value, type, isSyntactic);
	}
}
