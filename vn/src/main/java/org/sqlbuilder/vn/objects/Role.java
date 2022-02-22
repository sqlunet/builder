package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.*;

import java.util.Comparator;
import java.util.Objects;

public class Role implements Insertable, HasId
{
	public static final Comparator<Role> COMPARATOR = Comparator.comparing(Role::getClazz).thenComparing(Role::getRestrRole);

	public static final SetCollector<Role> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final RestrainedRole restrainedRole;

	private final VnClass clazz;

	// C O N S T R U C T O R

	public static Role make(final VnClass clazz, final RestrainedRole restrainedRole)
	{
		var m = new Role(clazz, restrainedRole);
		COLLECTOR.add(m);
		return m;
	}

	private Role(final VnClass clazz, final RestrainedRole restrainedRole)
	{
		this.restrainedRole = restrainedRole;
		this.clazz = clazz;
	}

	// A C C E S S

	public RestrainedRole getRestrRole()
	{
		return restrainedRole;
	}

	public RoleType getRoleType()
	{
		return restrainedRole.getRoleType();
	}

	public VnClass getClazz()
	{
		return clazz;
	}

	@ProvidesIdTo(type = Role.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
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
		Role that = (Role) o;
		return restrainedRole.equals(that.restrainedRole) && clazz.equals(that.clazz);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(restrainedRole, clazz);
	}

	// I N S E R T

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = RoleType.class)
	@RequiresIdFrom(type = Restrs.class)
	@Override
	public String dataRow()
	{
		Restrs restrs = restrainedRole.getRestrs();
		Integer restrsid = restrs == null ? null : restrs.getIntId();
		return String.format("%d,%d,%s", clazz.getIntId(), //
				restrainedRole.getRoleType().getIntId(), //
				Utils.nullableInt(restrsid) //
		);
	}

	@Override
	public String comment()
	{
		var r = restrainedRole.getRestrs();
		return String.format("%s,%s,%s", clazz.getName(), restrainedRole.getRoleType().getType(), //
				r == null ? "∅" : r.toString());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return "class=" + clazz + " role=" + restrainedRole;
	}
}
