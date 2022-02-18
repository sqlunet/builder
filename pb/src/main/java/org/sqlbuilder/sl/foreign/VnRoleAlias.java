package org.sqlbuilder.sl.foreign;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class VnRoleAlias implements Insertable
{
	public static Set<VnRoleAlias> SET = new HashSet<>();

	private final PbRole pbRole;

	private final VnRole vnRole;

	// C O N S T R U C T O R

	public static VnRoleAlias make(final PbRole role, final VnRole vnRole)
	{
		var m = new VnRoleAlias(role, vnRole);
		boolean wasThere = !SET.add(m);
		if (wasThere)
		{
			System.err.println();
			System.err.println(m);
		}
		return m;
	}

	private VnRoleAlias(final PbRole role, final VnRole vnRole)
	{
		this.pbRole = role;
		this.vnRole = vnRole;
	}

	// A C C E S S

	public PbRole getPbRole()
	{
		return pbRole;
	}

	public VnRole getVnRole()
	{
		return vnRole;
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
		VnRoleAlias that = (VnRoleAlias) o;
		return pbRole.equals(that.pbRole) && vnRole.equals(that.vnRole);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(pbRole, vnRole);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// rolesetid,roleid,vnclassid,vnroleid,vnclass,vntheta
		return String.format("'%s','%s','%s','%s'", //
				pbRole.roleSetName, //
				pbRole.arg, //
				vnRole.vnClass, //
				vnRole.theta.getTheta());
	}

	// R E S O L V E

	//	@Override
	//	public Pair<String, String> resolving()
	//	{
	//		return new Pair<>(vnRole.getVnClass().getClassTag(), vnRole.getVnTheta().getTheta());
	//	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s > %s", pbRole, vnRole);
	}
}
