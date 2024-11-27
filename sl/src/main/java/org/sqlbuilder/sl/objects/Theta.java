package org.sqlbuilder.sl.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;

import java.util.Comparator;
import java.util.Objects;

public class Theta implements HasId, Comparable<Theta>, Insertable
{
	public static final Comparator<Theta> COMPARATOR = Comparator.comparing(Theta::getTheta);

	public static final SetCollector<Theta> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String theta;

	// C O N S T R U C T O R

	public static Theta make(final String thetaName)
	{
		var t = new Theta(Utils.camelCase(thetaName));
		COLLECTOR.add(t);
		return t;
	}

	private Theta(final String thetaName)
	{
		this.theta = normalize(thetaName);
	}

	// A C C E S S

	public String getTheta()
	{
		return theta;
	}

	@RequiresIdFrom(type = Theta.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.apply(this);
	}

	@RequiresIdFrom(type = Theta.class)
	public static Integer getIntId(final Theta theta)
	{
		return theta == null ? null : COLLECTOR.apply(theta);
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
		Theta theta1 = (Theta) o;
		return theta.equals(theta1.theta);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(theta);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final Theta that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("'%s'", theta);
	}

	private static String normalize(final String thetaName)
	{
		return thetaName.substring(0, 1).toUpperCase() + thetaName.substring(1).toLowerCase();
	}

}
