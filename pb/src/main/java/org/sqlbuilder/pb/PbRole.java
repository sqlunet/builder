package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

import java.io.Serializable;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class PbRole implements Insertable<PbRole>, Comparable<PbRole>, Serializable
{
	public static final SortedSet<PbRole> SET = new TreeSet<>();

	public static Map<PbRole, Integer> MAP;

	private final PbRoleSet roleSet;

	private final String argN;

	private final PbFunc f;

	private final PbTheta theta;

	private final String descr;

	// C O N S T R U C T O R

	public PbRole(final PbRoleSet roleSet, final String n, final String f, final String theta, final String descriptor)
	{
		this.roleSet = roleSet;
		this.argN = n;
		this.f = f == null || f.isEmpty() ? null : new PbFunc(f);
		this.theta = theta == null || theta.isEmpty() ? null : new PbTheta(theta);
		this.descr = descriptor;
	}

	public static PbRole make(final PbRoleSet roleSet, final String n, final String f, final String theta, final String descriptor)
	{
		return new PbRole(roleSet, n, f, theta, descriptor);
	}

	// I D E N T I T Y

	@Override
	public int hashCode()
	{
		return 13 * this.roleSet.hashCode() + 19 * this.argN.hashCode() + (this.f == null ? 0 : this.f.hashCode());
	}

	@Override
	public boolean equals(final Object r)
	{
		if (!(r instanceof PbRole))
		{
			return false;
		}
		return compareTo((PbRole) r) == 0;
	}

	// O R D E R I N G

	@Override
	public int compareTo(final PbRole r)
	{
		final int c = this.roleSet.compareTo(r.roleSet);
		if (c != 0)
		{
			return c;
		}
		final int c2 = this.argN.compareTo(r.argN);
		if (c2 != 0 || this.f == null || r.f == null)
		{
			return c2;
		}
		return this.f.compareTo(r.f);
	}

	// A C C E S S

	public PbRoleSet getRoleSet()
	{
		return this.roleSet;
	}

	public String getArg()
	{
		return this.argN;
	}

	public String getDesc()
	{
		return this.descr;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// final long roleId = PbRole.map.getId(this);
		// final Long fId = this.f == null ? null : PbFunc.funcMap.get(this.f);
		// final Long thetaId = this.theta == null ? null : PbTheta.thetaMap.get(this.theta);

		// Long(1, roleId);
		// String(2, this.argN);
		// Null(3, Types.INTEGER);
		// Long(3, fId);
		// Null(4, Types.VARCHAR);
		// Long(4, thetaId);
		// String(5, this.descr);
		// Long(6, PbRoleSet.map.getId(this.roleSet));
		return null;
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		if (this.descr == null)
		{
			return String.format("%s[%s]", this.roleSet, this.argN);
		}
		return String.format("%s[%s-{%s}]", this.roleSet, this.argN, this.descr);
	}
}
