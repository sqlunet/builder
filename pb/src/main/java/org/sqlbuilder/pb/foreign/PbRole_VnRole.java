package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.Resolvable;
import org.sqlbuilder.pb.objects.Role;
import org.sqlbuilder.pb.objects.RoleSet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PbRole_VnRole implements Insertable, Resolvable<String,Integer>
{
	static public final Comparator<PbRole_VnRole> COMPARATOR = Comparator.comparing(PbRole_VnRole::getRole).thenComparing(PbRole_VnRole::getVnRole);

	public static Set<PbRole_VnRole> SET = new HashSet<>();

	private final Role role;

	private final VnRole vnRole;

	// C O N S T R U C T O R

	public static PbRole_VnRole make(final Role role, final VnRole vnRole)
	{
		var m = new PbRole_VnRole(role, vnRole);
		boolean wasThere = ! SET.add(m);
		if (wasThere)
		{
			System.err.println();
			System.err.println(m);
		}
		return m;
	}

	private PbRole_VnRole(final Role role, final VnRole vnRole)
	{
		this.role = role;
		this.vnRole = vnRole;
	}

	// A C C E S S

	public Role getRole()
	{
		return role;
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
		PbRole_VnRole that = (PbRole_VnRole) o;
		return role.equals(that.role) && vnRole.equals(that.vnRole);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(role, vnRole);
	}

	// I N S E R T

	@RequiresIdFrom(type = Role.class)
	@RequiresIdFrom(type = RoleSet.class)
	@Override
	public String dataRow()
	{
		// rolesetid,roleid,vnclassid,vnroleid,vnclass,vntheta
		return String.format("%d,%d,'%s','%s'", //
				role.getRoleSet().getIntId(), //
				role.getIntId(), //
				vnRole.getVnClass().getClassName(), //
				vnRole.getVnTheta().getTheta());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", //
				role.getRoleSet().getName(), //
				role);
	}

	// R E S O L V E

	@Override
	public String resolving()
	{
		return null;
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s > %s", role, vnRole);
	}
}
