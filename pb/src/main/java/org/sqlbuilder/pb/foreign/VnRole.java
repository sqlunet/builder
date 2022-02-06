package org.sqlbuilder.pb.foreign;

public class VnRole implements Comparable<VnRole>
{
	private final VnClass vnClass;

	private final String vnTheta;

	// C O N S T R U C T O R

	public VnRole(final VnClass vnClass, final String vnTheta)
	{
		this.vnClass = vnClass;
		this.vnTheta = vnTheta;
	}

	public static VnRole make(final VnClass vnClass, final String vnTheta)
	{
		return new VnRole(vnClass, vnTheta);
	}

	// A C C E S S

	public VnClass getVnClass()
	{
		return this.vnClass;
	}

	public String getVnTheta()
	{
		return this.vnTheta;
	}

	// I D E N T I T Y

	@Override
	public int hashCode()
	{
		return 31 * (this.vnClass == null ? 0 : this.vnClass.hashCode()) + 7 * (this.vnTheta == null ? 0 : this.vnTheta.hashCode());
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final VnRole other = (VnRole) obj;
		if (this.vnClass == null)
		{
			if (other.vnClass != null)
				return false;
		}
		else if (!this.vnClass.equals(other.vnClass))
			return false;
		if (this.vnTheta == null)
			return other.vnTheta == null;
		else
			return this.vnTheta.equals(other.vnTheta);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnRole other)
	{
		final int c = this.vnClass.compareTo(other.vnClass);
		if (c != 0)
			return c;
		return this.vnTheta.compareTo(other.vnTheta);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s[%s]", this.vnClass, this.vnTheta);
	}
}
