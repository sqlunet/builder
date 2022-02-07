package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder.vn.objects.Role;

import java.util.HashSet;
import java.util.Set;

public class Class_Role implements Insertable
{
	public static final Set<Class_Role> SET = new HashSet<>();

	private final Role role;

	private final VnClass clazz;

	// C O N S T R U C T

	public static Class_Role make(final VnClass clazz, final Role role)
	{
		var m = new Class_Role(clazz, role);
		SET.add(m);
		return m;
	}

	private Class_Role(final VnClass clazz, final Role role)
	{
		this.role = role;
		this.clazz = clazz;
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s",
				clazz.getName(),
				role.getRoleType());
	}

	// I N S E R T

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = Role.class)
	@Override
	public String dataRow()
	{
		return String.format("%d,%d",
				clazz.getIntId(),
				role.getIntId());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return "class=" + clazz + " role=" + role;
	}
}
