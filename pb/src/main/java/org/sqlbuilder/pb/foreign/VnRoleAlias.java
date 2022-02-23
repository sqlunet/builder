package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.Resolvable;
import org.sqlbuilder.pb.objects.Role;
import org.sqlbuilder.pb.objects.RoleSet;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Triplet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class VnRoleAlias implements Insertable, Resolvable<Pair<String, String>, Triplet<Integer, Integer, Integer>>
{
	static public final Comparator<VnRoleAlias> COMPARATOR = Comparator.comparing(VnRoleAlias::getRole).thenComparing(VnRoleAlias::getVnRole);

	public static final Set<VnRoleAlias> SET = new HashSet<>();

	public static final Function<Triplet<Integer, Integer, Integer>, String> RESOLVE_RESULT_STRINGIFIER = r -> //
			r == null ? "NULL,NULL,NULL" : String.format("%s,%s,%s", r.first, r.second, r.third);

	private final Role role;

	private final VnRole vnRole;

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static VnRoleAlias make(final Role role, final VnRole vnRole)
	{
		var m = new VnRoleAlias(role, vnRole);
		/* boolean wasThere = ! */ SET.add(m);
		/*
		if (wasThere)
		{
			System.err.printf("%nduplicate %s%n", m);
		}
		*/
		return m;
	}

	private VnRoleAlias(final Role role, final VnRole vnRole)
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
		VnRoleAlias that = (VnRoleAlias) o;
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
				vnRole.getVnClass().getClassTag(), //
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
	public Pair<String, String> resolving()
	{
		return new Pair<>(vnRole.getVnClass().getClassTag(), vnRole.getVnTheta().getTheta());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s > %s", role, vnRole);
	}
}
