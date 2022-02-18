package org.sqlbuilder.sl.foreign;

import org.sqlbuilder.pb.objects.Theta;

import java.util.Comparator;
import java.util.Objects;

public class VnRole implements Comparable<VnRole>
{
	static final Comparator<VnRole> COMPARATOR = Comparator.comparing(VnRole::getVnClass).thenComparing(VnRole::getTheta);

	public final String vnClass;

	public final Theta theta;

	public static VnRole make(final String vnClass, final Theta theta)
	{
		return new VnRole(vnClass, theta);
	}

	private VnRole(final String vnClass, final Theta theta)
	{
		this.vnClass = vnClass;
		this.theta = theta;
	}

	// A C C E S S

	public String getVnClass()
	{
		return vnClass;
	}

	public Theta getTheta()
	{
		return theta;
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
		VnRole vnRole = (VnRole) o;
		return vnClass.equals(vnRole.vnClass) && theta.equals(vnRole.theta);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(vnClass, theta);
	}

	// O R D E R

	@Override
	public int compareTo(final VnRole that)
	{
		return COMPARATOR.compare(this, that);
	}
}
