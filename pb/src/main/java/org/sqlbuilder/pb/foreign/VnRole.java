package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.pb.objects.Theta;

import java.util.Comparator;
import java.util.Objects;

public class VnRole implements Comparable<VnRole>
{
	static public final Comparator<VnRole> COMPARATOR = Comparator.comparing(VnRole::getVnClass).thenComparing(VnRole::getVnTheta);

	private final VnClass vnClass;

	private final Theta vnTheta;

	// C O N S T R U C T O R

	public static VnRole make(final VnClass vnClass, final Theta vnTheta)
	{
		return new VnRole(vnClass, vnTheta);
	}

	private VnRole(final VnClass vnClass, final Theta vnTheta)
	{
		this.vnClass = vnClass;
		this.vnTheta = vnTheta;
	}

	// A C C E S S

	public VnClass getVnClass()
	{
		return this.vnClass;
	}

	public Theta getVnTheta()
	{
		return this.vnTheta;
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
		return vnClass.equals(vnRole.vnClass) && vnTheta.equals(vnRole.vnTheta);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(vnClass, vnTheta);
	}

	// O R D E R I N G

	public int compareTo(@NotNull final VnRole that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s[%s]", this.vnClass, this.vnTheta);
	}
}
