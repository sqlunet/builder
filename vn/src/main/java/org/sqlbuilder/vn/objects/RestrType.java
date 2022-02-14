package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;

import java.util.Comparator;
import java.util.Objects;

public class RestrType implements HasId, Insertable, Comparable<RestrType>
{
	public static Comparator<RestrType> COMPARATOR = Comparator.comparing(RestrType::getType).thenComparing(RestrType::getValue).thenComparing(RestrType::isSyntactic);

	public static final SetCollector<RestrType> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String value;

	private final String type;

	final boolean isSyntactic;

	// C O N S T R U C T O R
	public static RestrType make(final String value, final String type, final boolean isSyntactic)
	{
		var r = new RestrType(value, type, isSyntactic);
		COLLECTOR.add(r);
		return r;
	}

	private RestrType(final String value, final String type, final boolean isSyntactic0)
	{
		this.value = value;
		this.type = type;
		this.isSyntactic = isSyntactic0;
	}

	// A C C E S S

	public String getValue()
	{
		return value;
	}

	public String getType()
	{
		return type;
	}

	public boolean isSyntactic()
	{
		return isSyntactic;
	}

	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
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
		RestrType that = (RestrType) o;
		return isSyntactic == that.isSyntactic && value.equals(that.value) && type.equals(that.type);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(value, type, isSyntactic);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final RestrType that)
	{
		return COMPARATOR.compare(this, that);
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
