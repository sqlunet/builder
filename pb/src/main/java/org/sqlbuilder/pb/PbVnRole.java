package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class PbVnRole implements Insertable, Comparable<PbVnRole>, Serializable
{
	protected static final Map<PbVnRole,Integer> MAP = new TreeMap<>();

	private final PbVnClass vnClass;

	private final String vnTheta;

	// C O N S T R U C T O R

	public PbVnRole(final PbVnClass vnClass, final String vnTheta)
	{
		this.vnClass = vnClass;
		this.vnTheta = vnTheta;
	}

	public static PbVnRole make(final PbVnClass vnClass, final String vnTheta)
	{
		return new PbVnRole(vnClass, vnTheta);
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
		final PbVnRole other = (PbVnRole) obj;
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
	public int compareTo(final PbVnRole other)
	{
		final int c = this.vnClass.compareTo(other.vnClass);
		if (c != 0)
			return c;
		return this.vnTheta.compareTo(other.vnTheta);
	}

	// A C C E S S

	public PbVnClass getVnClass()
	{
		return this.vnClass;
	}

	public String getVnTheta()
	{
		return this.vnTheta;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// System.out.printf("%s %s%n", PbVnRole.MAP.getId(this), toString());
		return null;
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s[%s]", this.vnClass, this.vnTheta);
	}
}
