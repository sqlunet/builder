package org.sqlbuilder.pb.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.pb.foreign.VnRole;
import org.sqlbuilder.pb.objects.Role;
import org.sqlbuilder.pb.objects.RoleSet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class PbRole_VnRole implements Insertable
{
	static public final Comparator<PbRole_VnRole> COMPARATOR = Comparator.comparing(PbRole_VnRole::getRole).thenComparing(PbRole_VnRole::getVnRole);

	public static Set<PbRole_VnRole> SET = new HashSet<>();

	private final Role role;

	private final VnRole vnRole;

	// C O N S T R U C T O R

	public static PbRole_VnRole make(final Role role, final VnRole vnRole)
	{
		var m = new PbRole_VnRole(role, vnRole);
		SET.add(m);
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

	// I N S E R T

	@RequiresIdFrom(type = Role.class)
	@RequiresIdFrom(type = RoleSet.class)
	@Override
	public String dataRow()
	{
		// rolesetid,roleid,vnclassid,vnroleid,vnclass,vntheta
		return String.format("%d,%d,%s,%s,'%s','%s'", //
				role.getRoleSet().getIntId(), //
				role.getIntId(), //
				"NULL", //
				"NULL", //
				vnRole.getVnClass().getClassName(), //
				vnRole.getVnTheta());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", //
				role.getRoleSet().getName(), //
				role.getArgn());
	}

	@Override
	public String toString()
	{
		return String.format("%s > %s", role, vnRole);
	}
}
