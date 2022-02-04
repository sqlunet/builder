package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;

import java.util.Comparator;

public class Theta implements HasId, Comparable<Theta>, Insertable
{
	public static final Comparator<Theta> COMPARATOR = Comparator.comparing(Theta::getTheta);

	public static final SetCollector<Theta> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String theta;

	// C O N S T R U C T O R

	public static Theta make(final String thetaName)
	{
		var t = new Theta(thetaName);
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
		return COLLECTOR.get(this);
	}

	@RequiresIdFrom(type=Theta.class)
	public static Integer getIntId(final Theta theta)
	{
		return theta == null ? null : COLLECTOR.get(theta);
	}

	// O R D E R

	@Override
	public int compareTo(final Theta that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return null;
	}

	private static String normalize(final String thetaName)
	{
		return thetaName.substring(0, 1).toUpperCase() + thetaName.substring(1).toLowerCase();
	}

}
